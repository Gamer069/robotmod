package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;

public class HitNearestEntityAction extends CustomAction {
	public static final int HIT_RADIUS = 8;

	@Override
	public void run(RobotEntity robot) {
		Box box = new Box(
			robot.getX() - HIT_RADIUS, robot.getY() - HIT_RADIUS, robot.getZ() - HIT_RADIUS,
			robot.getX() + HIT_RADIUS, robot.getY() + HIT_RADIUS, robot.getZ() + HIT_RADIUS
		);

		List<Entity> entities = robot.getWorld().getEntitiesByClass(Entity.class, box, e -> !e.equals(robot));

		Entity nearest = entities.stream()
			.filter(e -> e.squaredDistanceTo(robot) <= HIT_RADIUS * HIT_RADIUS)
			.min(Comparator.comparingDouble(e -> e.squaredDistanceTo(robot)))
			.orElse(null);

		if (nearest != null) {
			double baseDamage = robot.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
			double baseKnockback = robot.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK);

			nearest.damage((ServerWorld) robot.getWorld(),
				robot.getWorld().getDamageSources().mobAttackNoAggro(robot),
				(float) baseDamage);

			double dx = nearest.getX() - robot.getX();
			double dz = nearest.getZ() - robot.getZ();
			double distance = Math.sqrt(dx * dx + dz * dz);
			if (distance == 0) distance = 0.01;

			nearest.setVelocity(
				dx / distance * baseKnockback,
				0.2, // small vertical knockback
				dz / distance * baseKnockback
			);
			nearest.velocityModified = true;
		}
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_hit_nearest_entity";
	}
}
