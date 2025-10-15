package me.illia.robotmod.actions;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.registry.ModRegistries;

public class ActionRunner {
	public static void run(Action action, RobotEntity robot, int actionI) {
		Robotmod.LOGGER.info("running actoin");
		CustomAction customAction = ModRegistries.ACTION_TYPE.get(action.getActionType());
		Robotmod.LOGGER.info("running actoin1");

		if (customAction == null) return;

		Robotmod.LOGGER.info("running actoin2");

		customAction.params = action.getParams();

		Robotmod.LOGGER.info("running actoin3");

		robot.actionI = actionI;

		Robotmod.LOGGER.info("running actoin4");

		customAction.run(robot);

		Robotmod.LOGGER.info("running actoin5");
	}
}
