package me.illia.robotmod.world.dimension;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public record Dimension(RegistryKey<DimensionOptions> options, RegistryKey<World> world, RegistryKey<DimensionType> type) {
}
