package me.illia.robotmod.attachment;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record TeleportPoint(String name, BlockPos pos, RegistryKey<World> world) {
}
