package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class UseAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ItemStack stack = robot.inv.getStack(robot.slot);
		FakePlayer entity = FakePlayer.get((ServerWorld) robot.getWorld());
		entity.setCurrentHand(Hand.MAIN_HAND);
		entity.setStackInHand(Hand.MAIN_HAND, stack);

		Vec3d robotPos = robot.getPos();
		entity.setPos(robotPos.x, robotPos.y, robotPos.z);
		entity.setYaw(robot.getYaw());
		entity.setPitch(robot.getPitch());

		HitResult blockHitResult = robot.raycast(entity.getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE), 0, false);
		HitResult entityHitResult = robot.raycast(entity.getAttributeValue(EntityAttributes.BLOCK_INTERACTION_RANGE), 0, false);

		if (blockHitResult instanceof BlockHitResult result) {
			ActionResult actionResult = stack.useOnBlock(new ItemUsageContext(robot.getWorld(), entity, Hand.MAIN_HAND, stack, result));

			if (actionResult != ActionResult.PASS) {
				return;
			}
		}

		if (entityHitResult instanceof EntityHitResult result && result.getEntity() instanceof LivingEntity livingEntity) {
			ActionResult actionResult = stack.useOnEntity(entity, livingEntity, Hand.MAIN_HAND);

			if (actionResult != ActionResult.PASS) {
				return;
			}
		}

		stack.use(robot.getWorld(), entity, Hand.MAIN_HAND);
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_use";
	}
}
