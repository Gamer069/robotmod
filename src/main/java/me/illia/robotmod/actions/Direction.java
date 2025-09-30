package me.illia.robotmod.actions;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

public enum Direction implements StringIdentifiable {
	North,
	East,
	South,
	West;

	public static final Codec<Direction> CODEC = StringIdentifiable.createCodec(Direction::values);

	@Override
	public String asString() {
		return switch (this) {
			case North -> "direction.robotmod.north";
			case East -> "direction.robotmod.east";
			case South -> "direction.robotmod.south";
			case West -> "direction.robotmod.west";
		};
	}
}
