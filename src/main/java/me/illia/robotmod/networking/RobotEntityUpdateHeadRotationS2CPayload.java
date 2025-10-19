package me.illia.robotmod.networking;

import me.illia.robotmod.Util;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RobotEntityUpdateHeadRotationS2CPayload(int eid, float yaw, float pitch) implements CustomPayload {
	public static final Identifier ENTITY_UPDATE_HEAD_ROTATION_ID = Util.id("entity_update_head_rotation");
	public static final Id<RobotEntityUpdateHeadRotationS2CPayload> ID = new Id<>(ENTITY_UPDATE_HEAD_ROTATION_ID);
	public static final PacketCodec<RegistryByteBuf, RobotEntityUpdateHeadRotationS2CPayload> ENTITY_UPDATE_HEAD_ROTATION_PC = PacketCodec.tuple(PacketCodecs.INTEGER, RobotEntityUpdateHeadRotationS2CPayload::eid, PacketCodecs.FLOAT, RobotEntityUpdateHeadRotationS2CPayload::yaw, PacketCodecs.FLOAT, RobotEntityUpdateHeadRotationS2CPayload::pitch, RobotEntityUpdateHeadRotationS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
