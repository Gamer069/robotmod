package me.illia.robotmod.debug;

import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.registry.ModRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
//? if >=1.21.10 {
/*import net.minecraft.world.debug.DebugDataStore;
*///?}

import java.util.ArrayList;

public class ActionDebugRenderer implements DebugRenderer.Renderer {
	public ArrayList<RobotEntity> entities;
	public boolean enabled;
	public MinecraftClient client;

	public ActionDebugRenderer() {
		enabled = false;
		entities = new ArrayList<>();
		client = MinecraftClient.getInstance();
	}

	//? if <1.21.10 {
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
	//?} else {
	/*@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ, DebugDataStore store, Frustum frustum) {
	*///?}
		if (enabled) {
			double line = client.textRenderer.fontHeight * 0.03;

			for (RobotEntity entity : entities) {
				if (entity.actionI != -1) {
					int actionI = entity.actionI;
					Identifier action = entity.actions.get(actionI).getActionType();

					CustomAction customAction = ModRegistries.ACTION_TYPE.get(action);
					String customActionTranslation = customAction.translation();
					MutableText customActionText = Text.translatable(customActionTranslation);
					DebugRenderer.drawString(matrices, vertexConsumers, "curAction: " + customActionText.getString(), entity.getX(), entity.getY() + entity.getHeight() + 1, entity.getZ(), 0xFFFFFFFF);
					DebugRenderer.drawString(matrices, vertexConsumers, "actionAmount: " + entity.actions.size(), entity.getX(), entity.getY() + entity.getHeight() + 1 + line, entity.getZ(), 0xFFFFFFFF);
					DebugRenderer.drawString(matrices, vertexConsumers, "actionI: " + actionI, entity.getX(), entity.getY() + entity.getHeight() + 1 + line * 2, entity.getZ(), 0xFFFFFFFF);
				} else {
					DebugRenderer.drawString(matrices, vertexConsumers, "curAction: null", entity.getX(), entity.getY() + entity.getHeight() + 1, entity.getZ(), 0xFFFFFFFF);
					DebugRenderer.drawString(matrices, vertexConsumers, "actionAmount: " + entity.actions.size(), entity.getX(), entity.getY() + entity.getHeight() + 1 + line, entity.getZ(), 0xFFFFFFFF);
					DebugRenderer.drawString(matrices, vertexConsumers, "actionI: -1", entity.getX(), entity.getY() + entity.getHeight() + 1 + line * 2, entity.getZ(), 0xFFFFFFFF);
				}

//				DebugRenderer.drawString(matrices, vertexConsumers, "action", entity.getX(), entity.getY() + entity.getHeight(), entity.getZ(), 0xFFFFFFFF);
			}
		}
	}
}
