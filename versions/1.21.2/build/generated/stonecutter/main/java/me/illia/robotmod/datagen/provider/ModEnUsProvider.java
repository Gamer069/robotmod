package me.illia.robotmod.datagen.provider;

import me.illia.robotmod.Util;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.entity.ModEntities;
import me.illia.robotmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnUsProvider extends FabricLanguageProvider {
	public ModEnUsProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder b) {
		Util.add(b, "menu.robotmod.robot", "Robot");
		Util.add(b, "menu.robotmod.add", "+");
		Util.add(b, "menu.robotmod.no_points", "No Teleport Points!");

		Util.add(b, "menu.robotmod.action_type", "Type");
		Util.add(b, "menu.robotmod.action_type_walk", "Walk");
		Util.add(b, "menu.robotmod.action_type_walk_around", "Walk Around");
		Util.add(b, "menu.robotmod.action_type_harvest", "Harvest");
		Util.add(b, "menu.robotmod.action_type_wait", "Wait");
		Util.add(b, "menu.robotmod.action_type_home", "Home");
		Util.add(b, "menu.robotmod.action_type_switch_slot", "Switch Slot");
		Util.add(b, "menu.robotmod.action_type_set_yaw", "Set Yaw");
		Util.add(b, "menu.robotmod.action_type_set_pitch", "Set Pitch");
		Util.add(b, "menu.robotmod.action_type_break_block", "Break Block");
		Util.add(b, "menu.robotmod.action_type_drop", "Drop");
		Util.add(b, "menu.robotmod.action_type_say", "Say");
		Util.add(b, "menu.robotmod.action_type_hit_nearest_entity", "Hit Nearest Entity");


		Util.add(b, "menu.robotmod.action_param_walk_around", "around ");
		Util.add(b, "menu.robotmod.action_param_wait_sec", "for seconds");
		Util.add(b, "menu.robotmod.action_param_switch_slot", " to ");
		Util.add(b, "menu.robotmod.action_param_walk_to", " to ");
		Util.add(b, "menu.robotmod.action_param_walk_blocks", " for ");
		Util.add(b, "menu.robotmod.action_param_say", "text");
		Util.add(b, "menu.robotmod.action_param_yaw", " to ");
		Util.add(b, "menu.robotmod.action_param_pitch", " to ");
		Util.add(b, "menu.robotmod.action_param_break_fluid", "Break fluids?");


		Util.add(b, "bool.robotmod.true", "True");
		Util.add(b, "bool.robotmod.false", "False");
		Util.add(b, "menu.robotmod.actions", "Actions");
		Util.add(b, "menu.robotmod.tp", "Teleport");
		Util.add(b, "direction.robotmod.north", "North");
		Util.add(b, "direction.robotmod.east", "East");
		Util.add(b, "direction.robotmod.south", "South");
		Util.add(b, "direction.robotmod.west", "West");
		Util.add(b, "itemGroup.robotmod.robotmod", "Technology");

		Util.add(b, ModItems.TELEPORTER, "Ender Teleporter");
		Util.add(b, ModItems.PACKED_ENDER_PEARL, "Packed Ender Pearl");
		Util.add(b, ModItems.ROBOT_SPAWN_EGG, "Robot Spawn Egg");
		Util.add(b, ModBlocks.TELEPORTER_BLOCK, "Ender Teleporter Block");
		Util.add(b, ModBlocks.LUNAR_PANEL_BLOCK, "Lunar Panel Block");
		Util.add(b, ModEntities.ROBOT, "Robot");
	}
}
