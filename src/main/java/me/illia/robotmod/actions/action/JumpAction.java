package me.illia.robotmod.actions.action;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;

import java.util.List;

public class JumpAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		robot.jump();
		robot.velocityModified = true;
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_jump";
	}
}
