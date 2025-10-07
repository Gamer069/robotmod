package me.illia.robotmod.entity;

import net.minecraft.util.Hand;
import net.minecraft.item.ItemStack;

//? if >1.21.3 {
/*import net.minecraft.client.render.entity.state.ArmedEntityRenderState;

public class RobotEntityRenderState extends ArmedEntityRenderState {
	public Hand activeHand = Hand.MAIN_HAND;
	public ItemStack heldStack = ItemStack.EMPTY;
}
*///?} else {
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

public class RobotEntityRenderState extends LivingEntityRenderState {
	public Hand activeHand = Hand.MAIN_HAND;
	public ItemStack heldStack = ItemStack.EMPTY;
}
//?}
