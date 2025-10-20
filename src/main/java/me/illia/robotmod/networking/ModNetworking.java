package me.illia.robotmod.networking;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.block.TeleporterBlock;
import me.illia.robotmod.debug.DebugRenderers;
import me.illia.robotmod.entity.*;
import me.illia.robotmod.item.TeleporterItem;
import me.illia.robotmod.screen.ChooseTeleportScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Set;

public class ModNetworking {
	@SuppressWarnings("UnstableApiUsage")
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(RequestTeleportC2SPayload.ID, (requestTeleportC2SPayload, context) -> {
			BlockState state = Util.entityWorld(context.player()).getBlockState(requestTeleportC2SPayload.pos());
			if (state.getBlock() == ModBlocks.TELEPORTER_BLOCK && state.get(TeleporterBlock.CHARGED) && context.player() instanceof ServerPlayerEntity) {
				context.server().execute(() -> {
					context.player().teleport(context.server().getWorld(requestTeleportC2SPayload.world()), requestTeleportC2SPayload.pos().getX() + 0.5, requestTeleportC2SPayload.pos().getY() + 0.4, requestTeleportC2SPayload.pos().getZ() + 0.5, Set.of(), 0, 0, true);
				});
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(RobotActionsSyncC2SPayload.ID, ((robotActionsSyncC2SPayload, context) -> {
			MinecraftServer server = context.server();
			ServerWorld world = server.getWorld(robotActionsSyncC2SPayload.world());
			if (world == null) {
				return;
			}

			Entity entity = world.getEntityById(robotActionsSyncC2SPayload.eid());
			if (entity instanceof RobotEntity robot) {
				robot.save(new ArrayList<>(robotActionsSyncC2SPayload.actions()));
			}
		}));

		ServerPlayNetworking.registerGlobalReceiver(GetTeleportPointsC2SPayload.ID, ((getTeleportPointsC2SPayload, context) -> {
			MinecraftServer server = context.server();
			ServerWorld world = server.getWorld(getTeleportPointsC2SPayload.world());
			TeleportPointAttachedData data = world.getAttachedOrElse(ModAttachmentTypes.TELEPORT_POINTS, TeleportPointAttachedData.DEFAULT);
			context.responseSender().sendPacket(new GetTeleportPointsS2CPayload(data));
		}));

		ClientPlayNetworking.registerGlobalReceiver(GetTeleportPointsS2CPayload.ID, (((getTeleportPointsS2CPayload, context) -> {
			TeleportPointAttachedData.DATA = getTeleportPointsS2CPayload.data();

			MinecraftClient client = context.client();
			client.execute(() -> {
				// If the player previously requested points via the item, open the chooser immediately
				if (TeleporterItem.sentPacket) {
					client.setScreen(new ChooseTeleportScreen(TeleportPointAttachedData.DATA));
					// reset the flag so future uses behave normally
					TeleporterItem.sentPacket = false;
					return;
				}

				// Otherwise, if the chooser is open, refresh it with the new data
				if (client.currentScreen instanceof ChooseTeleportScreen) {
					client.setScreen(new ChooseTeleportScreen(TeleportPointAttachedData.DATA));
				}
			});
		})));

		ClientPlayNetworking.registerGlobalReceiver(UpdateActionDebugS2CPayload.ID, ((((updateActionDebugS2CPayload, context) -> {
			MinecraftClient client = context.client();
			//? if <1.21.10 {
			/*((DebugRenderers)client.debugRenderer).robotmod$getActionDebugRenderer().entities.stream().filter(e -> e.getId() == updateActionDebugS2CPayload.eid()).findFirst().ifPresent(robot -> robot.actionI = updateActionDebugS2CPayload.actionI());
			*///?} else {
			((DebugRenderers)client.worldRenderer.debugRenderer).robotmod$getActionDebugRenderer().entities.stream().filter(e -> e.getId() == updateActionDebugS2CPayload.eid()).findFirst().ifPresent(robot -> robot.actionI = updateActionDebugS2CPayload.actionI());
			//?}
		}))));

		ClientPlayNetworking.registerGlobalReceiver(UpdateHeldItemS2CPayload.ID, ((updateHeldItemS2CPayload, context) -> {
			MinecraftClient client = context.client();
			Entity entity = client.world.getEntityById(updateHeldItemS2CPayload.eid());

			Robotmod.LOGGER.info("Entity: " + entity);
			Robotmod.LOGGER.info("eid: " + updateHeldItemS2CPayload.eid());

			EntityRenderer<?, ?> renderer = client.getEntityRenderDispatcher().getRenderer(entity);
			if (renderer instanceof RobotEntityRenderer robotEntityRenderer && entity instanceof RobotEntity robotEntity) {
				robotEntity.inv.setStack(robotEntity.slot, updateHeldItemS2CPayload.heldItem());
				RobotEntityRenderState state = robotEntityRenderer.getAndUpdateRenderState(robotEntity, 0);
				robotEntityRenderer.updateRenderState(robotEntity, state, 0);
			}
		}));

		ClientPlayNetworking.registerGlobalReceiver(RobotEntityUpdateHeadRotationS2CPayload.ID, ((entityUpdateHeadRotationS2CPayload, context) -> {
			MinecraftClient client = context.client();
			Entity entity = client.world.getEntityById(entityUpdateHeadRotationS2CPayload.eid());

			if (entity == null) return;

			RobotEntity robot = (RobotEntity)entity;

			robot.setHeadPitch(entityUpdateHeadRotationS2CPayload.pitch());
			robot.setHeadYaw(entityUpdateHeadRotationS2CPayload.yaw());
		}));
	}
}
