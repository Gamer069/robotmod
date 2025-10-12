package me.illia.robotmod.screen;

import com.mojang.serialization.Codec;
import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.codec.PacketCodecs;

import java.util.ArrayList;

public class ModScreenHandlers {
	public static ExtendedScreenHandlerType<RobotScreenHandler, RobotScreenHandlerData> ROBOT_SCREEN_HANDLER = Util.<RobotScreenHandler, RobotScreenHandlerData>extendedScreenHandler(Util.id("robot_sh"), RobotScreenHandler::new, PacketCodecs.codec(RobotScreenHandlerData.CODEC));

	public static void init() {
		Robotmod.LOGGER.info("Initializing screen handlers for " + Robotmod.MODID);
	}
}
