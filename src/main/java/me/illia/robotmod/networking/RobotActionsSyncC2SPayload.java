package me.illia.robotmod.networking;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public record RobotActionsSyncC2SPayload(int eid, List<Action> actions, RegistryKey<World> world) implements CustomPayload {
	public static final Identifier ROBOT_ACTIONS_SYNC_ID = Util.id("robot_actions");
	public static final CustomPayload.Id<RobotActionsSyncC2SPayload> ID = new Id<>(ROBOT_ACTIONS_SYNC_ID);
	public static final PacketCodec<ByteBuf, RobotActionsSyncC2SPayload> ROBOT_ACTIONS_SYNC_CODEC = PacketCodec.tuple(PacketCodecs.codec(Codec.INT), RobotActionsSyncC2SPayload::eid, PacketCodecs.codec(Action.CODEC.codec().listOf()), RobotActionsSyncC2SPayload::actions, PacketCodecs.codec(World.CODEC), RobotActionsSyncC2SPayload::world, RobotActionsSyncC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
