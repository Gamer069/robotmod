package me.illia.robotmod.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class RobotScreenHandler extends ScreenHandler {
	public int eid;

	public RobotScreenHandler(int syncId, int eid) {
		super(ModScreenHandlers.ROBOT_SCREEN_HANDLER, syncId);
		this.eid = eid;
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
	}

	public int getEid() {
		return eid;
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return true;
	}
}
