package me.illia.robotmod.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public abstract class PlainHandledScreen<T extends ScreenHandler> extends HandledScreen<T> {
	public PlainHandledScreen(T handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	public boolean renderTitle() {
		return true;
	}
}
