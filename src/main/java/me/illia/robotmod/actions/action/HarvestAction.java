package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.block.CropBlock;

import java.util.List;

public class HarvestAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		if (robot.getWorld().getBlockState(robot.getBlockPos().up()).getBlock() instanceof CropBlock) {
			robot.getWorld().breakBlock(robot.getBlockPos().up(), true);
		}
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_harvest";
	}
}
