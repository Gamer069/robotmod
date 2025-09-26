package me.illia.robotmod.entity;

import com.mojang.datafixers.util.Pair;
import me.illia.robotmod.Robotmod;
import me.illia.robotmod.actions.Action;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.ArrayList;
import java.util.List;

public class ExecuteTask<E extends RobotEntity> extends ExtendedBehaviour<E> {
	public ExecuteTask() {
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryModuleState>> getMemoryRequirements() {
		return List.of();
	}

	@Override
	protected void tick(E entity) {
		Robotmod.LOGGER.info(entity.actions.toString());
	}
}
