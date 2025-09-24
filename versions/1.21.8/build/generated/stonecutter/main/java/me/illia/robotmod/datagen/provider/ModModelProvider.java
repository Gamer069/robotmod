package me.illia.robotmod.datagen.provider;

import me.illia.robotmod.Util;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.block.TeleporterBlock;
import me.illia.robotmod.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		Identifier chargedModelId = Util.id("block/teleporter_charged");
		Identifier unchargedModelId = Util.id("block/teleporter");

		blockStateModelGenerator.blockStateCollector.accept(
			VariantsBlockModelDefinitionCreator.of(ModBlocks.TELEPORTER_BLOCK)
				.with(BlockStateVariantMap.models(TeleporterBlock.CHARGED)
					.register(true, BlockStateModelGenerator.createWeightedVariant(chargedModelId))
					.register(false, BlockStateModelGenerator.createWeightedVariant(unchargedModelId)))
		);
	}

	@Override
	public void generateItemModels(ItemModelGenerator gen) {
		Util.itemModels(gen, ModItems.TELEPORTER, ModItems.PACKED_ENDER_PEARL, ModItems.ROBOT_SPAWN_EGG);
	}
}
