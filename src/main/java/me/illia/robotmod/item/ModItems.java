package me.illia.robotmod.item;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class ModItems {
	public static final SpawnEggItem ROBOT_SPAWN_EGG = Util.regSpawnEggItem(
		Util.id("robot_spawn_egg"),
		SpawnEggItem::new,
		ModEntities.ROBOT,
		new Item.Settings()
	);

	public static void init() {
		Robotmod.LOGGER.info("Initializing items for " + Robotmod.MODID);
	}
}
