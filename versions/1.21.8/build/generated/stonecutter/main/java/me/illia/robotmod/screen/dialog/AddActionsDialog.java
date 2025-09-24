package me.illia.robotmod.screen.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.dialog.body.DialogBody;

public record AddActionsDialog(int typeId) implements DialogBody {
	public static final MapCodec<AddActionsDialog> ADD_ACTIONS_DIALOG_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("typeId").forGetter(AddActionsDialog::typeId)
	).apply(inst, AddActionsDialog::new));

	@Override
	public MapCodec<? extends DialogBody> getTypeCodec() {
		return ADD_ACTIONS_DIALOG_CODEC;
	}
}
