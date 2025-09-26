package me.illia.robotmod.block;

import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.attachment.TeleportPoint;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TeleporterBlock extends SlabBlock {
	public static final BooleanProperty CHARGED = BooleanProperty.of("charged");
	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 0.4, 1);

	public TeleporterBlock(Settings settings) {
		super(settings);

		setDefaultState(getDefaultState().with(CHARGED, false));
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (stack.getItem() == ModItems.PACKED_ENDER_PEARL) {
			ServerWorld serverWorld = (ServerWorld)world;
			TeleportPointAttachedData data = serverWorld.getAttachedOrCreate(ModAttachmentTypes.TELEPORT_POINTS);
			if (data.findPointByPos(pos).isPresent()) return ActionResult.CONSUME;

			world.setBlockState(pos, state.with(CHARGED, true));
			world.playSound(player, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0f, 0.3f);

			ItemStack stack2 = stack.copy();
			stack2.decrementUnlessCreative(1, player);

			player.setStackInHand(hand, stack2);

			serverWorld.setAttached(ModAttachmentTypes.TELEPORT_POINTS, data.addPoint(new TeleportPoint("" + (data.points().size() + 1), pos, world.getRegistryKey())));

			return ActionResult.CONSUME;
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (player.getStackInHand(player.getActiveHand()).isEmpty() && world.getBlockState(pos).get(CHARGED)) {
			player.giveItemStack(new ItemStack(ModItems.PACKED_ENDER_PEARL));
			world.setBlockState(pos, state.with(CHARGED, false));

			ServerWorld serverWorld = (ServerWorld)world;
			TeleportPointAttachedData data = serverWorld.getAttachedOrCreate(ModAttachmentTypes.TELEPORT_POINTS);
			serverWorld.setAttached(ModAttachmentTypes.TELEPORT_POINTS, data.removePointByPos(pos));
		}

		return super.onUse(state, world, pos, player, hit);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		ServerWorld serverWorld = (ServerWorld)world;
		TeleportPointAttachedData data = serverWorld.getAttachedOrCreate(ModAttachmentTypes.TELEPORT_POINTS);
		serverWorld.setAttached(ModAttachmentTypes.TELEPORT_POINTS, data.removePointByPos(pos));

		return super.onBreak(world, pos, state, player);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CHARGED);
		super.appendProperties(builder);
	}
}
