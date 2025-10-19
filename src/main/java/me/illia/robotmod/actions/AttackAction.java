package me.illia.robotmod.actions;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.action.HitNearestEntityAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

import java.util.List;

public class AttackAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		robot.swingHand(Hand.MAIN_HAND);

		if (robot.raycast(HitNearestEntityAction.HIT_RADIUS, 0, false) instanceof EntityHitResult res) {
			robot.tryAttack((ServerWorld)Util.entityWorld(robot), res.getEntity());
		}
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_attack";
	}
}
