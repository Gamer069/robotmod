package me.illia.robotmod.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.illia.robotmod.Util;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UpdateActionDebugS2CPayload(int actionI, int eid) implements CustomPayload {
	public static final Identifier UPDATE_ACTION_DEBUG_ID = Util.id("update_action_debug");
	public static final CustomPayload.Id<UpdateActionDebugS2CPayload> ID = new Id<>(UPDATE_ACTION_DEBUG_ID);
	public static final PacketCodec<ByteBuf, UpdateActionDebugS2CPayload> UPDATE_ACTION_DEBUG_CODEC = PacketCodec.tuple(PacketCodecs.codec(Codec.INT), UpdateActionDebugS2CPayload::actionI, PacketCodecs.codec(Codec.INT), UpdateActionDebugS2CPayload::eid, UpdateActionDebugS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
