package me.illia.robotmod.actions;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.registry.ModRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ActionRunner {
	public static void run(Action action, RobotEntity robot, int actionI) {
		ServerWorld world = (ServerWorld)Util.entityWorld(robot);
		MinecraftServer server = world.getServer();
		long time = world.getTime();

		if (robot.waiting) {
			server.getSaveProperties().getMainWorldProperties().getScheduledEvents().setEvent("run_action_after_wait_" + actionI, robot.waitEndTick, new ExecuteActionCallback(robot.getId(), actionI, world.getRegistryKey()));
		}

		CustomAction customAction = ModRegistries.ACTION_TYPE.get(action.getActionType());

		if (customAction == null) return;

		customAction.params = action.getParams();

		robot.actionI = actionI;

		if (robot.waiting && time >= robot.waitStartTick && time < robot.waitEndTick) {
			robot.waiting = false;
			return;
		}

		customAction.run(robot);
	}

	public static void stopFor(RobotEntity robot, int ticks) {
		long time = Util.entityWorld(robot).getTime();
		robot.waitStartTick = time;
		robot.waitEndTick = time + ticks;
		robot.waiting = true;
	}
}
