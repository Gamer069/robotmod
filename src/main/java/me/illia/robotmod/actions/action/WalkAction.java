package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.*;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public class WalkAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor walkParamDesc = paramDescriptors().get(0);
		Action.ParamValue walkVal = params.get(Util.key(walkParamDesc.name()));
		Direction dir;
		if (walkVal instanceof Action.ParamValue.DirParam(Direction dirValue)) {
			dir = dirValue;
		} else {
			throw new RuntimeException("param isn't dir for some reason, instead it's " + walkVal.type());
		}

		ActionParamDescriptor blocksParamDesc = paramDescriptors().get(1);
		Action.ParamValue blocksVal = params.get(Util.key(blocksParamDesc.name()));
		float blocks;
		if (blocksVal instanceof Action.ParamValue.FloatParam(float blocksValue)) {
			blocks = blocksValue;
		} else {
			throw new RuntimeException("block amount isn't float for some reason, instead it's " + blocksVal.type());
		}

		net.minecraft.util.math.Direction mcDir = switch (dir) {
			case North -> net.minecraft.util.math.Direction.NORTH;
			case East -> net.minecraft.util.math.Direction.EAST;
			case South -> net.minecraft.util.math.Direction.SOUTH;
			case West -> net.minecraft.util.math.Direction.WEST;
		};

		Vec3d pos = robot.getBlockPos().toCenterPos().offset(mcDir, blocks);
		robot.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.0f);
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_walk_to"), ActionParamType.Dir), new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_walk_blocks"), ActionParamType.Float));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_walk";
	}
}
