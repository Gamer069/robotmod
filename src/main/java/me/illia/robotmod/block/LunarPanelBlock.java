package me.illia.robotmod.block;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LunarPanelBlock extends Block {
	public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 0.3, 1);

	public LunarPanelBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ACTIVE);
		super.appendProperties(builder);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		world.scheduleBlockTick(pos, this, 20);
	}

	@Override
	protected VoxelShape getCullingShape(BlockState state) {
		return SHAPE;
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (Util.night(world)) {
			world.setBlockState(pos, state.with(ACTIVE, true));
		} else {
			world.setBlockState(pos, state.with(ACTIVE, false));
		}

		world.scheduleBlockTick(pos, this, 20);

		super.scheduledTick(state, world, pos, random);
	}
}
