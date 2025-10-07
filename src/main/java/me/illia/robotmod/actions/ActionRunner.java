package me.illia.robotmod.actions;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.RobotEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static me.illia.robotmod.actions.ActionType.*;

public class ActionRunner {
	public static final int HIT_RADIUS = 8;

	public static void run(Action action, RobotEntity robot) {
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
					throw new RuntimeException("radius isn't int for some reason, instead it's " + val.type());
				}

				EntityNavigation nav = robot.getNavigation();

				BlockPos center = robot.getBlockPos();
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
					if (index.get() < path.size() && Util.night(robot.getWorld())) {
						Vec3d target = path.get(index.get());
						if (!nav.isFollowingPath()) {
							nav.startMovingTo(target.x, target.y, target.z, speed);
							index.incrementAndGet(); // next point
						}
					}
				}));
			}
			case Harvest -> {
				if (robot.getWorld().getBlockState(robot.getBlockPos().up()).getBlock() instanceof CropBlock) {
					robot.getWorld().breakBlock(robot.getBlockPos().up(), true);
				}
			}
			case Wait -> {
				// TODO: implement wait
			}
			case Home -> {
				BlockPos home = robot.home;
				robot.getNavigation().startMovingTo(home.getX(), home.getY(), home.getZ(), 1.0);
			}
			case SetHome -> {
				robot.home = robot.getBlockPos();
			}
			case SwitchToSlot -> {
				ActionParamDescriptor slotParamDesc = paramDescs.get(0);
				Action.ParamValue val = params.get(Util.key(slotParamDesc.name()));
				int slot;
				if (val instanceof Action.ParamValue.IntParam(int value)) {
					slot = value;
				} else {
					throw new RuntimeException("slot index isn't int for some reason, instead it's " + val.type());
				}

				robot.slot = slot;
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
					throw new RuntimeException("block amount isn't int for some reason, instead it's " + blocksVal.type());
				}

				net.minecraft.util.math.Direction mcDir = switch (dir) {
					case North -> net.minecraft.util.math.Direction.NORTH;
					case East -> net.minecraft.util.math.Direction.EAST;
					case South -> net.minecraft.util.math.Direction.SOUTH;
					case West -> net.minecraft.util.math.Direction.WEST;
				};

				BlockPos pos = robot.getBlockPos().offset(mcDir, blocks);
				robot.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.0f);
			}
			case Drop -> {
				//? if > 1.21.3 {
				/*robot.dropItem(robot.inv.getStack(robot.slot), false, false);
				*///?} else {
				RegistryKey<World> world = robot.getWorld().getRegistryKey();
				ServerWorld serverWorld = robot.getServer().getWorld(world);
				robot.dropStack(serverWorld, robot.inv.getStack(robot.slot), 2);
				//?}
				robot.inv.setStack(robot.slot, ItemStack.EMPTY);
			}
			case Say -> {
				ActionParamDescriptor msgParamDesc = paramDescs.get(0);
				Action.ParamValue msgVal = params.get(Util.key(msgParamDesc.name()));
				String msg;
				if (msgVal instanceof Action.ParamValue.StringParam(String value)) {
					msg = value;
				} else {
					throw new RuntimeException("msg isn't string for some reason, instead it's " + msgVal.type());
				}

				Text text = robot.getName().copy().append(" > ").append(msg);

				for (PlayerEntity player : robot.getWorld().getPlayers()) {
					player.sendMessage(text, false);
				}
			}
			case HitNearestEntity -> {
				Box box = new Box(
					robot.getX() - HIT_RADIUS, robot.getY() - HIT_RADIUS, robot.getZ() - HIT_RADIUS,
					robot.getX() + HIT_RADIUS, robot.getY() + HIT_RADIUS, robot.getZ() + HIT_RADIUS
				);

				List<Entity> entities = robot.getWorld().getEntitiesByClass(Entity.class, box, e -> !e.equals(robot));

				Entity nearest = entities.stream()
					.filter(e -> e.squaredDistanceTo(robot) <= HIT_RADIUS * HIT_RADIUS)
					.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(robot)))
					.orElse(null);

				if (nearest != null) {
					double baseDamage = robot.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
					double baseKnockback = robot.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK);

					nearest.damage((ServerWorld) robot.getWorld(),
						robot.getWorld().getDamageSources().mobAttackNoAggro(robot),
						(float) baseDamage);

					double dx = nearest.getX() - robot.getX();
					double dz = nearest.getZ() - robot.getZ();
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
			case SetYaw -> {
				ActionParamDescriptor yawParamDesc = paramDescs.get(0);
				Action.ParamValue yawVal = params.get(Util.key(yawParamDesc.name()));
				float yaw;
				if (yawVal instanceof Action.ParamValue.FloatParam(float value)) {
					yaw = value;
				} else {
					throw new RuntimeException("yaw isn't float for some reason, instead it's " + yawVal.type());
				}

				robot.setYaw(yaw);
			}
			case SetPitch -> {
				ActionParamDescriptor pitchParamDesc = paramDescs.get(0);
				Action.ParamValue pitchVal = params.get(Util.key(pitchParamDesc.name()));
				float pitch;
				if (pitchVal instanceof Action.ParamValue.FloatParam(float value)) {
					pitch = value;
				} else {
					throw new RuntimeException("pitch isn't float for some reason, instead it's " + pitchVal.type());
				}

				robot.setPitch(pitch);
			}
			case BreakBlock -> {
				ActionParamDescriptor breakFluidDesc = paramDescs.get(0);
				Action.ParamValue breakFluidVal = params.get(Util.key(breakFluidDesc.name()));
				boolean breakFluid;
				if (breakFluidVal instanceof Action.ParamValue.BoolParam(boolean value)) {
					breakFluid = value;
				} else {
					throw new RuntimeException("break fluid isn't bool for some reason, instead it's " + breakFluidVal.type());
				}

				HitResult res = robot.raycast(5, 1.0F, breakFluid);
				if (res instanceof BlockHitResult blockHit) {
					BlockPos pos = blockHit.getBlockPos();
					BlockState state = robot.getWorld().getBlockState(pos);
//					float ticksToBreak = (state.getHardness(robot.getWorld(), pos) * 20) / robot.inv.getStack(robot.slot).getMiningSpeedMultiplier(state);

					// TODO: implement block breaking progress using setBlockBreakingInfo

					if (robot.getWorld().getBlockState(blockHit.getBlockPos()).getBlock() instanceof FluidBlock && breakFluid) {
						robot.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
					} else {
						robot.getWorld().breakBlock(pos, true);
					}
				}
			}
		}
	}
}
