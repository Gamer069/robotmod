package me.illia.robotmod;

import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.block.TeleporterBlock;
import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.item.ModItems;
import me.illia.robotmod.itemgroup.ModItemGroups;
import me.illia.robotmod.networking.RequestTeleportC2SPayload;
import me.illia.robotmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Robotmod implements ModInitializer {
	public static final String MODID = "robotmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		SharedConstants.isDevelopment = true;

		PayloadTypeRegistry.playC2S().register(RequestTeleportC2SPayload.ID, RequestTeleportC2SPayload.REQUEST_TELEPORT_CODEC);
		ModAttachmentTypes.init();
		ModScreenHandlers.init();
		ModItems.init();
		ModBlocks.init();
		ModItemGroups.init();
		ModEntities.init();
		FabricDefaultAttributeRegistry.register(ModEntities.ROBOT, RobotEntity.createMobAttributes().add(EntityAttributes.ARMOR_TOUGHNESS, 5).add(EntityAttributes.MAX_HEALTH, 8).build());

		ServerPlayNetworking.registerGlobalReceiver(RequestTeleportC2SPayload.ID, (requestTeleportC2SPayload, context) -> {
			BlockState state = context.player().getWorld().getBlockState(requestTeleportC2SPayload.pos());
			if (state.getBlock() == ModBlocks.TELEPORTER_BLOCK && state.get(TeleporterBlock.CHARGED) && context.player() instanceof ServerPlayerEntity) {
				Robotmod.LOGGER.info("done " + context.player().getName());
				context.server().execute(() -> {
					context.player().teleport(context.server().getWorld(requestTeleportC2SPayload.world()), requestTeleportC2SPayload.pos().getX() + 0.5, requestTeleportC2SPayload.pos().getY() + 0.4, requestTeleportC2SPayload.pos().getZ() + 0.5, Set.of(), 0, 0, true);
				});
			}
		});

		CustomPortalBuilder.beginPortal()
			.frameBlock(Blocks.WHITE_CONCRETE)
			.lightWithItem(Items.FLINT_AND_STEEL)
			.destDimID(Util.id("the_future"))
			.tintColor(184, 203, 194)
			.customIgnitionSource(PortalIgnitionSource.FIRE)
			.registerPortal();
	}
}
