package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

import java.util.List;

public class RobotInventoryScreenHandler extends ScreenHandler {
	public final PlayerInventory playerInv;
	public SimpleInventory inv;
	public List<ItemStack> stacks;
	public int eid;

	public RobotInventoryScreenHandler(int syncId, PlayerInventory playerInv, RobotScreenHandlerData robotScreenHandlerData) {
		super(ModScreenHandlers.ROBOT_INVENTORY_SCREEN_HANDLER, syncId);
		this.playerInv = playerInv;
		this.stacks = robotScreenHandlerData.stacks();
		this.eid = robotScreenHandlerData.eid();
		this.inv = new SimpleInventory(stacks.toArray(new ItemStack[]{})) {
			@Override
			public void markDirty() {
				World world = Util.entityWorld(playerInv.player);
				if (!world.isClient()) {
					RobotEntity entity = (RobotEntity)world.getEntityById(eid);
					for (int i = 0; i < heldStacks.size(); i++) {
						ItemStack stack = heldStacks.get(i);
						entity.inv.heldStacks.set(i, stack);
					}
				}
				super.markDirty();
			}
		};

		int half = (int) Math.ceil(stacks.size() / 2.0);
		for (int i = 0; i < stacks.size(); i++) {
			int x = 9 + 18 * (i % half);
			int y = (i < half) ? 123 : 141; // top row 110, bottom row 230
			addSlot(new Slot(inv, i, x, y));
		}
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return null;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return false;
	}
}
