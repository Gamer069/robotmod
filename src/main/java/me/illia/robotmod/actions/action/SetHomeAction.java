package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;

import java.util.List;

public class SetHomeAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		robot.home = robot.getBlockPos();
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_set_home";
	}
}
