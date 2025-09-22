package me.illia.robotmod.datagen;

import me.illia.robotmod.datagen.provider.ModEnUsProvider;
import me.illia.robotmod.datagen.provider.ModModelProvider;
import me.illia.robotmod.datagen.provider.ModRecipeProvider;
import me.illia.robotmod.datagen.provider.ModWorldProvider;
import me.illia.robotmod.world.dimension.ModDimensions;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class RobotmodDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModEnUsProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModWorldProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, ModDimensions::init);
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
	}
}