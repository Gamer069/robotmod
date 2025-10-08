package me.illia.robotmod;

import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.item.ModItems;
import me.illia.robotmod.itemgroup.ModItemGroups;
import me.illia.robotmod.networking.*;
import me.illia.robotmod.registry.ModRegistries;
import me.illia.robotmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Robotmod implements ModInitializer {
	public static final String MODID = "robotmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		SharedConstants.isDevelopment = true;

		PayloadTypeRegistry.playC2S().register(RequestTeleportC2SPayload.ID, RequestTeleportC2SPayload.REQUEST_TELEPORT_CODEC);
		PayloadTypeRegistry.playC2S().register(RobotActionsSyncC2SPayload.ID, RobotActionsSyncC2SPayload.ROBOT_ACTIONS_SYNC_CODEC);
		PayloadTypeRegistry.playC2S().register(GetTeleportPointsC2SPayload.ID, GetTeleportPointsC2SPayload.GET_TP_POINTS_CODEC);

		PayloadTypeRegistry.playS2C().register(GetTeleportPointsS2CPayload.ID, GetTeleportPointsS2CPayload.GET_TP_POINTS_CODEC);

		ModAttachmentTypes.init();
		ModScreenHandlers.init();
		ModItems.init();
		ModBlocks.init();
		ModItemGroups.init();
		ModEntities.init();
		ModNetworking.init();
		ModRegistries.init();
		FabricDefaultAttributeRegistry.register(ModEntities.ROBOT, RobotEntity.createMobAttributes().add(EntityAttributes.ARMOR_TOUGHNESS, 5).add(EntityAttributes.MAX_HEALTH, 8).add(EntityAttributes.ATTACK_DAMAGE, 2).add(EntityAttributes.ATTACK_KNOCKBACK, 1).build());

	}
}
