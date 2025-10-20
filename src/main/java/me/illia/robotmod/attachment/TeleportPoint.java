package me.illia.robotmod.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record TeleportPoint(String name, BlockPos pos, RegistryKey<World> world) {
	public static final Codec<TeleportPoint> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.STRING.fieldOf("name").forGetter(TeleportPoint::name),
		BlockPos.CODEC.fieldOf("pos").forGetter(TeleportPoint::pos),
		World.CODEC.fieldOf("world").forGetter(TeleportPoint::world)
	).apply(inst, TeleportPoint::new));
}
