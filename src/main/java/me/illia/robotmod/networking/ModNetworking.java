package me.illia.robotmod.networking;

import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.block.TeleporterBlock;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.item.TeleporterItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Set;

public class ModNetworking {
	@SuppressWarnings("UnstableApiUsage")
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(RequestTeleportC2SPayload.ID, (requestTeleportC2SPayload, context) -> {
			BlockState state = context.player().getWorld().getBlockState(requestTeleportC2SPayload.pos());
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
		})));
	}
}
