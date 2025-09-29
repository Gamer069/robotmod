package me.illia.robotmod.screen;

import com.mojang.serialization.Codec;
import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;

public class ModScreenHandlers {
//	public static ExtendedScreenHandlerType<RobotScreenHandler, ArrayList<Action>> ROBOT_SCREEN_HANDLER = Util.extendedScreenHandler(Util.id("robot_sh"), (syncId, playerInventory, data) -> new RobotScreenHandler(syncId, data), Util.ACTIONS_PC);
	public static ExtendedScreenHandlerType<RobotScreenHandler, Integer> ROBOT_SCREEN_HANDLER = Util.<RobotScreenHandler, Integer>extendedScreenHandler(Util.id("robot_sh"), (syncId, playerInventory, data) -> new RobotScreenHandler(syncId, data), PacketCodecs.codec(Codec.INT));

	public static void init() {
		Robotmod.LOGGER.info("Initializing screen handlers for " + Robotmod.MODID);
	}
}
