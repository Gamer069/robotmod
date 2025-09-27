package me.illia.robotmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class LunarPanelBlock extends Block {
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

	public LunarPanelBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.appendProperties(builder);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.getTime() > 13000 && world.getTime() < 23000) {
			world.setBlockState(pos, state.with(ACTIVE, true));
		} else {
			world.setBlockState(pos, state.with(ACTIVE, false));
		}

		super.randomTick(state, world, pos, random);
	}
}
