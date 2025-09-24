package me.illia.robotmod.world.dimension;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.Optional;

public class ModDimensions {
	public static final Dimension FUTURE = Util.dimKeys(Util.id("the_future"));

	public static void init(Registerable<DimensionType> ctx) {
		Robotmod.LOGGER.info("registering dimensions for " + Robotmod.MODID);

		Util.dim(
			ctx,
			FUTURE.type(),
			null,
			true,
			false,
			false,
			false,
			12,
			true,
			true,
			16,
			256,
			256,
			BlockTags.INFINIBURN_OVERWORLD,
			DimensionTypes.OVERWORLD_ID,
			1.0f,
			Optional.of(3),
			new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0)
		);
	}
}
