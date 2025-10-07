package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class HomeAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		BlockPos home = robot.home;
		robot.getNavigation().startMovingTo(home.getX(), home.getY(), home.getZ(), 1.0);
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_home";
	}
}
