package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;

public class DropAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		//? if > 1.21.3 {
		robot.dropItem(robot.inv.getStack(robot.slot), false, false);
		 //?} else {
		/*RegistryKey<World> world = robot.getWorld().getRegistryKey();
		ServerWorld serverWorld = robot.getServer().getWorld(world);
		robot.dropStack(serverWorld, robot.inv.getStack(robot.slot), 2);
		*///?}
		robot.inv.setStack(robot.slot, ItemStack.EMPTY);
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_drop";
	}
}
