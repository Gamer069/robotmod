package me.illia.robotmod.block;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;

public class ModBlocks {
	public static final Block TELEPORTER_BLOCK = Util.block(Util.id("teleporter"), TeleporterBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.TEAL));
	public static final Block LUNAR_PANEL_BLOCK = Util.block(Util.id("lunar_panel"), LunarPanelBlock::new, AbstractBlock.Settings.create().mapColor(MapColor.PALE_YELLOW));

	public static void init() {
		Robotmod.LOGGER.info("Registering mod blocks for " + Robotmod.MODID);
	}
}
