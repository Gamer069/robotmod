package me.illia.robotmod.itemgroup;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.item.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {
	public static final ItemGroup ROBOTMOD_GROUP = Util.itemGroup(
		Util.id("robotmod"),
		"itemGroup.robotmod.robotmod",
		new ItemStack(ModItems.PACKED_ENDER_PEARL),

		ModItems.ROBOT_SPAWN_EGG,
		ModItems.TELEPORTER,
		ModItems.PACKED_ENDER_PEARL,
		ModBlocks.TELEPORTER_BLOCK.asItem()
	);

	public static void init() {
		Robotmod.LOGGER.info("registering item groups for " + Robotmod.MODID);
	}
}
