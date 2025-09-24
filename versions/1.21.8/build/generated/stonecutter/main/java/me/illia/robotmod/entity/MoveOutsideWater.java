package me.illia.robotmod.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

public class MoveOutsideWater<E extends PathAwareEntity> extends ExtendedBehaviour<E> {
	private static final MemoryTest MEM_REQUIREMENTS = MemoryTest.builder(1).hasMemory(MemoryModuleType.IS_IN_WATER);
	private Path escapePath = null;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
		return MEM_REQUIREMENTS;
	}

	@Override
	protected boolean shouldRun(ServerWorld level, E entity) {
		return entity.isTouchingWater();
	}

	@Override
	protected void start(E entity) {
		World world = entity.getWorld();
		BlockPos start = entity.getBlockPos();

		BlockPos target = null;
		int radius = 16;
		for (BlockPos pos : BlockPos.iterateOutwards(start, radius, radius, radius)) {
			if (!world.getBlockState(pos).isLiquid() && world.isAir(pos.up())) {
				target = pos.toImmutable();
				break;
			}
		}

		if (target != null) {
			this.escapePath = entity.getNavigation().findPathTo(target, 1);
			if (this.escapePath != null)
				entity.getNavigation().startMovingAlong(this.escapePath, 1.2d); // movement speed
		}

		super.start(entity);
	}

	@Override
	protected void stop(E entity) {
		this.escapePath = null;
		BrainUtil.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		super.stop(entity);
	}
}
