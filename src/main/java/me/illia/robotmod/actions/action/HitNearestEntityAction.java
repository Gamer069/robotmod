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
			// casting to server world is acceptable because actions only get ran on server
			robot.tryAttack((ServerWorld) robot.getWorld(), nearest);
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
