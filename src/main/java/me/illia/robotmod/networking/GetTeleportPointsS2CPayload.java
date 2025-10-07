package me.illia.robotmod.networking;

import me.illia.robotmod.Util;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record GetTeleportPointsS2CPayload(TeleportPointAttachedData data) implements CustomPayload {
	public static final Identifier GET_TP_POINTS_ID = Util.id("get_tp_points");
	public static final CustomPayload.Id<GetTeleportPointsS2CPayload> ID = new Id<>(GET_TP_POINTS_ID);
	public static final PacketCodec<RegistryByteBuf, GetTeleportPointsS2CPayload> GET_TP_POINTS_CODEC = PacketCodec.tuple(Util.TELEPORT_POINTS_PC, GetTeleportPointsS2CPayload::data, GetTeleportPointsS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
