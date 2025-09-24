package me.illia.robotmod.screen;

import me.illia.robotmod.actions.Action;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import java.util.ArrayList;

public class RobotScreenHandler extends ScreenHandler {
	public ArrayList<Action> actions;

	public RobotScreenHandler(int syncId, ArrayList<Action> actions) {
		super(ModScreenHandlers.ROBOT_SCREEN_HANDLER, syncId);
		this.actions = actions;
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
	}

	public ArrayList<Action> getActions() {
		return actions;
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
