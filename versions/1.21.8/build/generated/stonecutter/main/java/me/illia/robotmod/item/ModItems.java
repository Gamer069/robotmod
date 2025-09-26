package me.illia.robotmod.item;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.ModEntities;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Rarity;

public class ModItems {
	//? if > 1.21.2 {
	public static final SpawnEggItem ROBOT_SPAWN_EGG = Util.spawnEgg(
		Util.id("robot_spawn_egg"),
		SpawnEggItem::new,
		ModEntities.ROBOT,
		new Item.Settings()
	);
	//?} else {
	/*public static final SpawnEggItem ROBOT_SPAWN_EGG = Util.spawnEgg(
		Util.id("robot_spawn_egg"),
		SpawnEggItem::new,
		ModEntities.ROBOT,
		0x4682B4,
		0xC0C0C0,
		new Item.Settings()
	);
	*///?}

	public static final Item TELEPORTER = Util.item(
		Util.id("teleporter_item"),
		TeleporterItem::new,
		new Item.Settings().maxDamage(64).rarity(Rarity.UNCOMMON)
	);

	public static final Item PACKED_ENDER_PEARL = Util.item(
		Util.id("packed_ender_pearl"),
		PackedEnderPearlItem::new,
		new Item.Settings().maxCount(8).rarity(Rarity.UNCOMMON).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
	);

	public static void init() {
		Robotmod.LOGGER.info("Initializing items for " + Robotmod.MODID);
	}
}
