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

	public RobotScreenHandler(int syncId, PlayerInventory playerInv, RobotScreenHandlerData data) {
		super(ModScreenHandlers.ROBOT_SCREEN_HANDLER, syncId);
		this.eid = data.eid();
		this.stacks = data.stacks();
		this.inv = new SimpleInventory(stacks.toArray(new ItemStack[]{})) {
			@Override
			public void markDirty() {
				World world = playerInv.player.getWorld();
				if (!world.isClient) {
					RobotEntity entity = (RobotEntity)world.getEntityById(eid);
					for (int i = 0; i < heldStacks.size(); i++) {
						ItemStack stack = heldStacks.get(i);
						entity.inv.heldStacks.set(i, stack);
					}
				}
				super.markDirty();
			}
		};
		this.playerInv = playerInv;

		int half = (int) Math.ceil(stacks.size() / 2.0);
		for (int i = 0; i < stacks.size(); i++) {
			int x = 9 + 18 * (i % half);
			int y = (i < half) ? 123 : 141; // top row 110, bottom row 230
			addSlot(new Slot(inv, i, x, y));
		}
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
