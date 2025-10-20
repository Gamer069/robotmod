package me.illia.robotmod;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import me.illia.robotmod.actions.ExecuteActionCallback;
import me.illia.robotmod.actions.ModActionTypes;
import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.debug.DebugRenderers;
import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.entity.UpdateActionDebugS2CPayload;
import me.illia.robotmod.entity.UpdateHeldItemS2CPayload;
import me.illia.robotmod.item.ModItems;
import me.illia.robotmod.itemgroup.ModItemGroups;
import me.illia.robotmod.networking.*;
import me.illia.robotmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.timer.TimerCallback;
import net.minecraft.world.timer.TimerCallbackSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Robotmod implements ModInitializer {
	public static final String MODID = "robotmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			SharedConstants.isDevelopment = true;
		}

		PayloadTypeRegistry.playC2S().register(RequestTeleportC2SPayload.ID, RequestTeleportC2SPayload.REQUEST_TELEPORT_CODEC);
		PayloadTypeRegistry.playC2S().register(RobotActionsSyncC2SPayload.ID, RobotActionsSyncC2SPayload.ROBOT_ACTIONS_SYNC_CODEC);

		PayloadTypeRegistry.playS2C().register(UpdateActionDebugS2CPayload.ID, UpdateActionDebugS2CPayload.UPDATE_ACTION_DEBUG_CODEC);
		PayloadTypeRegistry.playS2C().register(UpdateHeldItemS2CPayload.ID, UpdateHeldItemS2CPayload.UPDATE_HELD_ITEM_CODEC);
		PayloadTypeRegistry.playS2C().register(RobotEntityUpdateHeadRotationS2CPayload.ID, RobotEntityUpdateHeadRotationS2CPayload.ENTITY_UPDATE_HEAD_ROTATION_PC);

		ModAttachmentTypes.init();
		ModScreenHandlers.init();
		ModItems.init();
		ModBlocks.init();
		ModItemGroups.init();
		ModEntities.init();
		ModNetworking.init();
		ModActionTypes.init();

		ClientEntityEvents.ENTITY_LOAD.register(((entity, clientWorld) -> {
			if (!(entity instanceof RobotEntity robot)) return;
			MinecraftClient client = MinecraftClient.getInstance();
			//? if <1.21.10 {
			/*((DebugRenderers)client.debugRenderer).robotmod$getActionDebugRenderer().entities.add(robot);
			*///?} else {
			((DebugRenderers)client.worldRenderer.debugRenderer).robotmod$getActionDebugRenderer().entities.remove(robot);
			//?}
		}));

		ClientEntityEvents.ENTITY_UNLOAD.register((((entity, clientWorld) -> {
			if (!(entity instanceof RobotEntity robot)) return;
			MinecraftClient client = MinecraftClient.getInstance();
			//? if <1.21.10 {
			/*((DebugRenderers)client.debugRenderer).robotmod$getActionDebugRenderer().entities.remove(robot);
			*///?} else {
			((DebugRenderers)client.worldRenderer.debugRenderer).robotmod$getActionDebugRenderer().entities.remove(robot);
			//?}
		})));

		//? if >1.21.3 {
		TimerCallbackSerializer.INSTANCE.registerSerializer(Util.id("exec_action"), ExecuteActionCallback.CODEC);
		//? } else {
		/*TimerCallbackSerializer.INSTANCE.registerSerializer(new TimerCallback.Serializer<MinecraftServer, ExecuteActionCallback>(Util.id("exec_action"), ExecuteActionCallback.class) {
			@Override
			public void serialize(NbtCompound nbt, ExecuteActionCallback callback) {
				DataResult<NbtElement> res = ExecuteActionCallback.CODEC.encoder().encode(callback, NbtOps.INSTANCE, NbtOps.INSTANCE.empty());
				NbtElement encoded = res.getOrThrow();

				if (encoded instanceof NbtCompound compound) {
					nbt.copyFrom(compound);
				}
			}

			@Override
			public ExecuteActionCallback deserialize(NbtCompound nbt) {
				DataResult<ExecuteActionCallback> res = ExecuteActionCallback.CODEC.decoder()
					.decode(NbtOps.INSTANCE, nbt)
					.map(Pair::getFirst);

				return res.getOrThrow();
			}
		});
		*///? }
	}
}
