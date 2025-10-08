package me.illia.robotmod.mixin;

import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
	@Shadow
	private static void spawnEntity(World world, BlockPattern.Result patternResult, Entity entity, BlockPos pos) {}

	@Unique
	private BlockPattern robotPat;

	private BlockPattern getRobotPattern() {
		if (this.robotPat == null) {
			this.robotPat = BlockPatternBuilder.start()
				.aisle("P", "I", "I")
				.where('I', pos -> pos.getBlockState().isOf(Blocks.IRON_BLOCK))
				.where('P', CachedBlockPosition.matchesBlockState(CarvedPumpkinBlock.IS_GOLEM_HEAD_PREDICATE))
				.build();
		}

		return this.robotPat;
	}

	@Inject(method = "trySpawnEntity", at = @At("TAIL"))
	private void trySpawnEntity(World world, BlockPos pos, CallbackInfo ci) {
		BlockPattern.Result result = getRobotPattern().searchAround(world, pos);

		if (result != null) {
			RobotEntity entity = ModEntities.ROBOT.create(world, SpawnReason.TRIGGERED);
			if (entity != null) {
				spawnEntity(world, result, entity, result.translate(0, 2, 0).getBlockPos());
			}
		}
	}
}
