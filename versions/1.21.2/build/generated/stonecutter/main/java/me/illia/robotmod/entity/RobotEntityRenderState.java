package me.illia.robotmod.entity;

import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class RobotEntityRenderState extends ArmedEntityRenderState {
	public Hand activeHand = Hand.MAIN_HAND;
	public ItemStack heldStack = ItemStack.EMPTY;
}
