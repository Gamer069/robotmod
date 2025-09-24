package me.illia.robotmod.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModWorldProvider extends FabricDynamicRegistryProvider {
	public ModWorldProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup, Entries entries) {
		entries.addAll(wrapperLookup.getOrThrow(RegistryKeys.DIMENSION_TYPE));
	}

	@Override
	public String getName() {
		return "World provider";
	}
}
