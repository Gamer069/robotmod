package me.illia.robotmod.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.timer.Timer;
import net.minecraft.world.timer.TimerCallback;

import java.util.List;

public record ExecuteActionCallback(int eid, int actionI, RegistryKey<World> world) implements TimerCallback<MinecraftServer> {
	public static final MapCodec<ExecuteActionCallback> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("eid").forGetter(ExecuteActionCallback::eid),
		Codec.INT.fieldOf("actionI").forGetter(ExecuteActionCallback::actionI),
		World.CODEC.fieldOf("world").forGetter(ExecuteActionCallback::world)
	).apply(inst, ExecuteActionCallback::new));

	@Override
	public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
		var worldInstance = server.getWorld(world);
		if (worldInstance == null) return; // world not loaded

		Entity entity = worldInstance.getEntityById(eid);
		if (!(entity instanceof RobotEntity robot)) return; // not a robot

		List<Action> actions = robot.actions;
		if (actionI < 0 || actionI >= actions.size()) return; // invalid index

		List<Action> remaining = actions.subList(actionI, actions.size());

		for (int i = 0; i < remaining.size(); i++) {
			Action action = remaining.get(i);
			ActionRunner.run(action, robot, i);
		}
	}

	//? if >1.21.3 {
	@Override
	public MapCodec<? extends ExecuteActionCallback> getCodec() {
		return CODEC;
	}
	//? }
}
