package me.illia.robotmod.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.illia.robotmod.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record UpdateHeldItemS2CPayload(int eid, ItemStack heldItem) implements CustomPayload {
	public static final Identifier UPDATE_HELD_ITEM_ID = Util.id("update_held_item");
	public static final CustomPayload.Id<UpdateHeldItemS2CPayload> ID = new Id<>(UPDATE_HELD_ITEM_ID);
	public static final PacketCodec<ByteBuf, UpdateHeldItemS2CPayload> UPDATE_HELD_ITEM_CODEC = PacketCodec.tuple(PacketCodecs.codec(Codec.INT), UpdateHeldItemS2CPayload::eid, PacketCodecs.codec(ItemStack.OPTIONAL_CODEC), UpdateHeldItemS2CPayload::heldItem, UpdateHeldItemS2CPayload::new);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
