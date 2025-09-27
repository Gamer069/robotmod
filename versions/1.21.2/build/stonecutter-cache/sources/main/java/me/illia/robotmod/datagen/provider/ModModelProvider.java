package me.illia.robotmod.datagen.provider;

import me.illia.robotmod.Util;
import me.illia.robotmod.block.LunarPanelBlock;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.block.TeleporterBlock;
import me.illia.robotmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
//? if >= 1.21.8 {
/*import net.minecraft.client.data.*;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;

public class ModModelProvider extends FabricModelProvider {
*///?} else {
import net.minecraft.data.client.*;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

public class ModModelProvider extends FabricModelProvider {
//?}

	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		Identifier chargedTeleporterModelId = Util.id("block/teleporter_charged");
		Identifier unchargedTeleporterModelId = Util.id("block/teleporter");

		blockStateModelGenerator.blockStateCollector.accept(
			//? if >= 1.21.8 {
			/*VariantsBlockModelDefinitionCreator.of(ModBlocks.TELEPORTER_BLOCK)
				.with(BlockStateVariantMap.models(TeleporterBlock.CHARGED)
					.register(true, BlockStateModelGenerator.createWeightedVariant(chargedTeleporterModelId))
					.register(false, BlockStateModelGenerator.createWeightedVariant(unchargedTeleporterModelId)))
			*///?} else {
			VariantsBlockStateSupplier.create(ModBlocks.TELEPORTER_BLOCK)
				.coordinate(
					BlockStateVariantMap.create(TeleporterBlock.CHARGED)
						.register(true, BlockStateVariant.create().put(VariantSettings.MODEL, chargedTeleporterModelId))
						.register(false, BlockStateVariant.create().put(VariantSettings.MODEL, unchargedTeleporterModelId))
				)
			//?}
		);

		Identifier activatedLunarPanelModelId = Util.id("block/lunar_panel_active");
		Identifier unactivatedLunarPanelModelId = Util.id("block/lunar_panel");

		blockStateModelGenerator.blockStateCollector.accept(
			//? if >= 1.21.8 {
			/*VariantsBlockModelDefinitionCreator.of(ModBlocks.LUNAR_PANEL_BLOCK)
				.with(BlockStateVariantMap.models(LunarPanelBlock.ACTIVE)
					.register(true, BlockStateModelGenerator.createWeightedVariant(activatedLunarPanelModelId))
					.register(false, BlockStateModelGenerator.createWeightedVariant(unactivatedLunarPanelModelId)))
			*///?} else {
			VariantsBlockStateSupplier.create(ModBlocks.LUNAR_PANEL_BLOCK)
				.coordinate(
					BlockStateVariantMap.create(LunarPanelBlock.ACTIVE)
						.register(true, BlockStateVariant.create().put(VariantSettings.MODEL, activatedLunarPanelModelId))
						.register(false, BlockStateVariant.create().put(VariantSettings.MODEL, unactivatedLunarPanelModelId))
				)
			//?}
		);
	}

	@Override
	public void generateItemModels(ItemModelGenerator gen) {
		Util.itemModels(gen, ModItems.TELEPORTER, ModItems.PACKED_ENDER_PEARL, ModItems.ROBOT_SPAWN_EGG);
	}
}
