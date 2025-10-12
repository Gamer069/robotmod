package me.illia.robotmod.screen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.List;

public record RobotScreenHandlerData(int eid, List<ItemStack> stacks) {
	public static final Codec<RobotScreenHandlerData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.INT.fieldOf("eid").forGetter(RobotScreenHandlerData::eid),
		ItemStack.OPTIONAL_CODEC.listOf().fieldOf("stacks").forGetter(RobotScreenHandlerData::stacks)
	).apply(inst, RobotScreenHandlerData::new));
}
