package me.illia.robotmod.networking;

import me.illia.robotmod.Util;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record RequestTeleportC2SPayload(BlockPos pos, RegistryKey<World> world) implements CustomPayload {
	public static final Identifier REQUEST_TELEPORT_ID = Util.id("request_teleport");
	public static final CustomPayload.Id<RequestTeleportC2SPayload> ID = new Id<>(REQUEST_TELEPORT_ID);
	public static final PacketCodec<RegistryByteBuf, RequestTeleportC2SPayload> REQUEST_TELEPORT_CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, RequestTeleportC2SPayload::pos, PacketCodecs.codec(World.CODEC), RequestTeleportC2SPayload::world, RequestTeleportC2SPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
