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

		int half = 9;
		for (int i = 0; i < stacks.size(); i++) {
			int x = 8 + 18 * (i % half);
			int y = (i < half) ? 18 : 36; // top row 110, bottom row 230
			int finalI = i;
			addSlot(new Slot(inv, finalI, x, y) {
				@Override
				public void markDirty() {
					RobotEntity robot = (RobotEntity)Util.entityWorld(playerInv.player).getEntityById(eid);
					robot.inv.setStack(finalI, this.getStack());
					super.markDirty();
				}
			});
		}

		addPlayerInv(playerInv);
		addPlayerHotbar(playerInv);
	}

	public void addPlayerInv(PlayerInventory inv) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(inv, j + (i + 1) * 9, 8 + j * 18, 56 + i * 18));
			}
		}
	}

	public void addPlayerHotbar(PlayerInventory inv) {
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(inv, i, 8 + i * 18, 114));
		}
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
