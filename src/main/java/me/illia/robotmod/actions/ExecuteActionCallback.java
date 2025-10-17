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

public record ExecuteActionCallback(int eid, int actionI, RegistryKey<World> world) implements TimerCallback<MinecraftServer> {
	public static final MapCodec<ExecuteActionCallback> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("eid").forGetter(ExecuteActionCallback::eid),
		Codec.INT.fieldOf("actionI").forGetter(ExecuteActionCallback::actionI),
		World.CODEC.fieldOf("world").forGetter(ExecuteActionCallback::world)
	).apply(inst, ExecuteActionCallback::new));

	@Override
	public void call(MinecraftServer server, Timer<MinecraftServer> events, long time) {
		Entity entity = server.getWorld(world).getEntityById(eid);
		if (entity instanceof RobotEntity robot) {
			ActionRunner.run(robot.actions.get(actionI), robot, actionI);
		}
	}

	@Override
	public MapCodec<? extends ExecuteActionCallback> getCodec() {
		return CODEC;
	}
}
