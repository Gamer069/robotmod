package me.illia.robotmod.actions;

import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.registry.ModRegistries;

public class ActionRunner {
	public static void run(Action action, RobotEntity robot, int actionI) {
		CustomAction customAction = ModRegistries.ACTION_TYPE.get(action.getActionType());

		if (customAction == null) return;

		customAction.params = action.getParams();

		robot.actionI = actionI;

		customAction.run(robot);
	}
}
