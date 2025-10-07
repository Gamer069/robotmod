package me.illia.robotmod.client;

import me.illia.robotmod.Util;
import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.entity.RobotEntityModel;
import me.illia.robotmod.entity.RobotEntityRenderer;
import me.illia.robotmod.screen.ModScreenHandlers;
import me.illia.robotmod.screen.RobotScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class RobotmodClient implements ClientModInitializer {

	public static final EntityModelLayer MODEL_ROBOT_LAYER = new EntityModelLayer(Util.id("robot"), "main");

	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.ROBOT_SCREEN_HANDLER, RobotScreen::new);
		EntityRendererRegistry.register(ModEntities.ROBOT, RobotEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_ROBOT_LAYER, RobotEntityModel::getTexturedModelData);
	}
}
