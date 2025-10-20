package me.illia.robotmod.entity;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;

public class ModEntities {
	public static final EntityType<RobotEntity> ROBOT = Util.entity(
		Util.id("robot"),
		EntityType.Builder
			.create(RobotEntity::new, SpawnGroup.MISC)
			.dimensions(1.0f, 1.4f)
	);
	public static void init() {
		Robotmod.LOGGER.info("Initializing entities for " + Robotmod.MODID);

		FabricDefaultAttributeRegistry.register(
			ROBOT,
			RobotEntity.createMobAttributes()
				.add(EntityAttributes.ARMOR_TOUGHNESS, 5)
				.add(EntityAttributes.MAX_HEALTH, 8)
				.add(EntityAttributes.ATTACK_DAMAGE, 2)
				.add(EntityAttributes.ATTACK_KNOCKBACK, 1)
				.build()
		);
	}
}
