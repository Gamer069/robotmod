package me.illia.robotmod.screen;

import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.List;

public class RobotScreenHandler extends ScreenHandler {
	public int eid;
	public List<ItemStack> stacks;
	public SimpleInventory inv;
	public PlayerInventory playerInv;

	public RobotScreenHandler(int syncId, RobotScreenHandlerData data) {
		super(ModScreenHandlers.ROBOT_SCREEN_HANDLER, syncId);
		this.eid = data.eid();
		this.stacks = data.stacks();
	}

	public int getEid() {
		return eid;
	}

	public List<ItemStack> getRobotStacks() {
		return stacks;
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
