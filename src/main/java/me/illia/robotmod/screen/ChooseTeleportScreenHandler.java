package me.illia.robotmod.screen;

import me.illia.robotmod.attachment.TeleportPointAttachedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class ChooseTeleportScreenHandler extends ScreenHandler {
	public PlayerInventory playerInv;
	public TeleportPointAttachedData data;

	public ChooseTeleportScreenHandler(int syncId, PlayerInventory playerInv, TeleportPointAttachedData data) {
		super(ModScreenHandlers.CHOOSE_TELEPORT_SCREEN_HANDLER, syncId);

		this.playerInv = playerInv;
		this.data = data;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
