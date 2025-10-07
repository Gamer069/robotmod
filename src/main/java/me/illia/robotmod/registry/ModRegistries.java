package me.illia.robotmod.registry;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.actions.action.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRegistries {
	public static final Registry<CustomAction> ACTION_TYPE = FabricRegistryBuilder.createSimple(ModRegistryKeys.ACTION_TYPE).attribute(RegistryAttribute.SYNCED).buildAndRegister();

	public static final Identifier WALK_AROUND = Util.id("walk_around");
	public static final Identifier HARVEST = Util.id("harvest");
	public static final Identifier WAIT = Util.id("wait");
	public static final Identifier HOME = Util.id("home");
	public static final Identifier SET_HOME = Util.id("set_home");
	public static final Identifier SWITCH_TO_SLOT = Util.id("switch_to_slot");
	public static final Identifier WALK = Util.id("walk");
	public static final Identifier DROP = Util.id("drop");
	public static final Identifier SAY = Util.id("say");
	public static final Identifier HIT_NEAREST_ENTITY = Util.id("hit_nearest_entity");
	public static final Identifier SET_YAW = Util.id("set_yaw");
	public static final Identifier SET_PITCH = Util.id("set_pitch");
	public static final Identifier BREAK_BLOCK = Util.id("break_block");

	public static void init() {
		Util.actionType(WALK_AROUND, new WalkAroundAction());
		Util.actionType(HARVEST, new HarvestAction());
		Util.actionType(WAIT, new WaitAction());
		Util.actionType(HOME, new HomeAction());
		Util.actionType(SET_HOME, new SetHomeAction());
		Util.actionType(SWITCH_TO_SLOT, new SwitchToSlotAction());
		Util.actionType(WALK, new WalkAction());
		Util.actionType(DROP, new DropAction());
		Util.actionType(SAY, new SayAction());
		Util.actionType(HIT_NEAREST_ENTITY, new HitNearestEntityAction());
		Util.actionType(SET_YAW, new SetYawAction());
		Util.actionType(SET_PITCH, new SetPitchAction());
		Util.actionType(BREAK_BLOCK, new BreakBlockAction());
	}
}
