package me.illia.robotmod.networking;

import me.illia.robotmod.Util;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public record GetTeleportPointsC2SPayload(RegistryKey<World> world) implements CustomPayload {
	public static final Identifier GET_TP_POINTS_ID = Util.id("get_tp_points");
	public static final CustomPayload.Id<GetTeleportPointsC2SPayload> ID = new Id<>(GET_TP_POINTS_ID);
	public static final PacketCodec<RegistryByteBuf, GetTeleportPointsC2SPayload> GET_TP_POINTS_CODEC = PacketCodec.tuple(PacketCodecs.codec(World.CODEC), GetTeleportPointsC2SPayload::world, GetTeleportPointsC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
