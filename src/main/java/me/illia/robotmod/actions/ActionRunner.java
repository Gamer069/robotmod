package me.illia.robotmod.actions;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.RobotEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static me.illia.robotmod.actions.ActionType.*;

public class ActionRunner {
	public static final int HIT_RADIUS = 8;

	public static void run(Action action, RobotEntity robotEntity) {
		List<ActionParamDescriptor> paramDescs = action.getActionType().getParams();
		HashMap<String, Action.ParamValue> params = action.getParams();

		switch (action.getActionType()) {
			case WalkAround -> {
				ActionParamDescriptor radiusParamDesc = paramDescs.get(0);
				Action.ParamValue val = params.get(Util.key(radiusParamDesc.name()));
				int r = 0;
				if (val instanceof Action.ParamValue.IntParam(int value)) {
					r = value;
				} else {
					throw new RuntimeException("param isn't int for some reason, instead it's " + val.type());
				}

				EntityNavigation nav = robotEntity.getNavigation();

				BlockPos center = robotEntity.getBlockPos();
				int segments = 72;
				double speed = 1.0;

				List<Vec3d> path = new ArrayList<>();
				for (int i = 0; i < segments; i++) {
					double angle = 2 * Math.PI * i / segments;
					double x = center.getX() + r * Math.sin(angle);
					double y = center.getY();
					double z = center.getZ() + r * Math.cos(angle);
					path.add(new Vec3d(x, y, z));
				}
				path.add(new Vec3d(center.getX(), center.getY(), center.getZ()));

				AtomicInteger index = new AtomicInteger(0);

				ServerTickEvents.START_SERVER_TICK.register((minecraftServer -> {
					if (index.get() < path.size() && Util.night(robotEntity.getWorld())) {
						Vec3d target = path.get(index.get());
						if (!nav.isFollowingPath()) {
							nav.startMovingTo(target.x, target.y, target.z, speed);
							index.incrementAndGet(); // next point
						}
					}
				}));
			}
			case Harvest -> {
				if (robotEntity.getWorld().getBlockState(robotEntity.getBlockPos().up()).getBlock() instanceof CropBlock) {
					robotEntity.getWorld().breakBlock(robotEntity.getBlockPos().up(), true);
				}
			}
			case Wait -> {
				// TODO: implement wait
			}
			case Home -> {
				BlockPos home = robotEntity.home;
				robotEntity.getNavigation().startMovingTo(home.getX(), home.getY(), home.getZ(), 1.0);
			}
			case SetHome -> {
				robotEntity.home = robotEntity.getBlockPos();
			}
			case SwitchToSlot -> {
				ActionParamDescriptor slotParamDesc = paramDescs.get(0);
				Action.ParamValue val = params.get(Util.key(slotParamDesc.name()));
				int slot;
				if (val instanceof Action.ParamValue.IntParam(int value)) {
					slot = value;
				} else {
					throw new RuntimeException("param isn't int for some reason, instead it's " + val.type());
				}

				robotEntity.slot = slot;
			}
			case Walk -> {
				ActionParamDescriptor walkParamDesc = paramDescs.get(0);
				Action.ParamValue walkVal = params.get(Util.key(walkParamDesc.name()));
				Direction dir;
				if (walkVal instanceof Action.ParamValue.DirParam(Direction dirValue)) {
					dir = dirValue;
				} else {
					throw new RuntimeException("param isn't dir for some reason, instead it's " + walkVal.type());
				}

				ActionParamDescriptor blocksParamDesc = paramDescs.get(1);
				Action.ParamValue blocksVal = params.get(Util.key(blocksParamDesc.name()));
				int blocks;
				if (blocksVal instanceof Action.ParamValue.IntParam(int blocksValue)) {
					blocks = blocksValue;
				} else {
					throw new RuntimeException("param isn't int for some reason, instead it's " + blocksVal.type());
				}

				net.minecraft.util.math.Direction mcDir = switch (dir) {
					case North -> net.minecraft.util.math.Direction.NORTH;
					case East -> net.minecraft.util.math.Direction.EAST;
					case South -> net.minecraft.util.math.Direction.SOUTH;
					case West -> net.minecraft.util.math.Direction.WEST;
				};

				BlockPos pos = robotEntity.getBlockPos().offset(mcDir, blocks);
				robotEntity.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.0f);
			}
			case Drop -> {
				robotEntity.dropItem(robotEntity.inv.getStack(robotEntity.slot), false, false);
				robotEntity.inv.setStack(robotEntity.slot, ItemStack.EMPTY);
			}
			case Say -> {
				ActionParamDescriptor msgParamDesc = paramDescs.get(0);
				Action.ParamValue msgVal = params.get(Util.key(msgParamDesc.name()));
				String msg;
				if (msgVal instanceof Action.ParamValue.StringParam(String value)) {
					msg = value;
				} else {
					throw new RuntimeException("param isn't string for some reason, instead it's " + msgVal.type());
				}

				Text text = robotEntity.getName().copy().append(" > ").append(msg);

				for (PlayerEntity player : robotEntity.getWorld().getPlayers()) {
					player.sendMessage(text, false);
				}
			}
			case HitNearestEntity -> {
				Box box = new Box(
					robotEntity.getX() - HIT_RADIUS, robotEntity.getY() - HIT_RADIUS, robotEntity.getZ() - HIT_RADIUS,
					robotEntity.getX() + HIT_RADIUS, robotEntity.getY() + HIT_RADIUS, robotEntity.getZ() + HIT_RADIUS
				);

				List<Entity> entities = robotEntity.getWorld().getEntitiesByClass(Entity.class, box, e -> !e.equals(robotEntity));

				Entity nearest = entities.stream()
					.filter(e -> e.squaredDistanceTo(robotEntity) <= HIT_RADIUS * HIT_RADIUS)
					.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(robotEntity)))
					.orElse(null);

				if (nearest != null) {
					double baseDamage = robotEntity.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
					double baseKnockback = robotEntity.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK);

					nearest.damage((ServerWorld) robotEntity.getWorld(),
						robotEntity.getWorld().getDamageSources().mobAttackNoAggro(robotEntity),
						(float) baseDamage);

					double dx = nearest.getX() - robotEntity.getX();
					double dz = nearest.getZ() - robotEntity.getZ();
					double distance = Math.sqrt(dx * dx + dz * dz);
					if (distance == 0) distance = 0.01;

					nearest.setVelocity(
						dx / distance * baseKnockback,
						0.2, // small vertical knockback
						dz / distance * baseKnockback
					);
					nearest.velocityModified = true;
				}
			}
		}
	}
}
