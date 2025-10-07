package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.block.LunarPanelBlock;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.entity.RobotEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WalkAroundAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor radiusParamDesc = paramDescriptors().get(0);
		Action.ParamValue val = params.get(Util.key(radiusParamDesc.name()));
		int r;
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
			if (Util.nearest(robot, 35, state -> state.isOf(ModBlocks.LUNAR_PANEL_BLOCK) && state.get(LunarPanelBlock.ACTIVE)) && index.get() < path.size()) {
				Vec3d target = path.get(index.get());
				if (!nav.isFollowingPath()) {
					nav.startMovingTo(target.x, target.y, target.z, speed);
					index.incrementAndGet(); // next point
				}
			};
		}));
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_walk_around"), ActionParamType.Int));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_walk_around";
	}
}
