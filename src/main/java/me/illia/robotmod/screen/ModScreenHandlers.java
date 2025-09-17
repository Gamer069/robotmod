package me.illia.robotmod.screen;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
	public static ScreenHandlerType<RobotScreenHandler> ROBOT_SCREEN_HANDLER = Util.regScreenHandler(Util.id("robot_sh"), (syncId, playerInventory) -> new RobotScreenHandler(syncId, 0));

	public static void init() {
		Robotmod.LOGGER.info("Initializing screen handlers for " + Robotmod.MODID);
	}
}
