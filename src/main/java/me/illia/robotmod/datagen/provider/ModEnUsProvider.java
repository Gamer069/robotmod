package me.illia.robotmod.datagen.provider;

import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnUsProvider extends FabricLanguageProvider {
	public ModEnUsProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add("menu.robotmod.robot", "Robot");
		translationBuilder.add("menu.robotmod.add", "+");
		translationBuilder.add("menu.robotmod.no_points", "No Teleport Points!");
		translationBuilder.add("menu.robotmod.tp", "Teleport");
		translationBuilder.add("itemGroup.robotmod.robotmod", "Technology");
		translationBuilder.add(ModItems.TELEPORTER, "Ender Teleporter");
		translationBuilder.add(ModItems.PACKED_ENDER_PEARL, "Packed Ender Pearl");
		translationBuilder.add(ModBlocks.TELEPORTER_BLOCK, "Ender Teleporter Block");
	}
}
