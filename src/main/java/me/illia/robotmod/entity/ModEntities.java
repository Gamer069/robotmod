package me.illia.robotmod.entity;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class ModEntities {
	public static final EntityType<RobotEntity> ROBOT = Util.entity(
		Util.id("robot"),
		EntityType.Builder
			.create(RobotEntity::new, SpawnGroup.MISC)
			.dimensions(1.0f, 1.4f)
	);
	public static void init() {
		Robotmod.LOGGER.info("Initializing entities for " + Robotmod.MODID);
	}
}
