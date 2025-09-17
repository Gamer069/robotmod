package me.illia.robotmod.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class RobotScreenHandler extends ScreenHandler {
	private int robotId;

	public RobotScreenHandler(int syncId, int robotId) {
		super(ModScreenHandlers.ROBOT_SCREEN_HANDLER, syncId);
		this.robotId = robotId;
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
