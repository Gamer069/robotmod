package me.illia.robotmod.entity;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class ModEntities {
	public static final EntityType<RobotEntity> ROBOT = Util.regEntity(
		Util.id("robot"),
		EntityType.Builder
			.create(RobotEntity::new, SpawnGroup.MISC)
			.dimensions(0.1f, 0.1f)
	);

	public static void init() {
		Robotmod.LOGGER.info("Initializing entities for " + Robotmod.MODID);
	}
}
