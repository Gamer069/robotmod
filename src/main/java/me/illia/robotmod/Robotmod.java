package me.illia.robotmod;

import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.entity.RobotEntity;
import me.illia.robotmod.item.ModItems;
import me.illia.robotmod.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.attribute.EntityAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Robotmod implements ModInitializer {
	public static final String MODID = "robotmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModScreenHandlers.init();
		ModItems.init();
		ModEntities.init();
		FabricDefaultAttributeRegistry.register(ModEntities.ROBOT, RobotEntity.createMobAttributes().add(EntityAttributes.ARMOR_TOUGHNESS, 5).add(EntityAttributes.MAX_HEALTH, 8).build());
	}
}
