package me.illia.robotmod.registry;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class BreakBlockAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor breakFluidDesc = paramDescriptors().get(0);
		Action.ParamValue breakFluidVal = params.get(Util.key(breakFluidDesc.name()));
		boolean breakFluid;
		if (breakFluidVal instanceof Action.ParamValue.BoolParam(boolean value)) {
			breakFluid = value;
		} else {
			throw new RuntimeException("break fluid isn't bool for some reason, instead it's " + breakFluidVal.type());
		}

		HitResult res = robot.raycast(5, 1.0F, breakFluid);
		if (res instanceof BlockHitResult blockHit) {
			BlockPos pos = blockHit.getBlockPos();

			// TODO: implement block breaking progress using setBlockBreakingInfo

			if (robot.getWorld().getBlockState(blockHit.getBlockPos()).getBlock() instanceof FluidBlock && breakFluid) {
				robot.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			} else {
				robot.getWorld().breakBlock(pos, true);
			}
		}
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_break_fluid"), ActionParamType.Bool));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_break_block";
	}
}
