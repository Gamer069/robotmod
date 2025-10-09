package me.illia.robotmod.actions;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.action.*;
import net.minecraft.util.Identifier;

public class ModActionTypes {
	public static final Identifier WALK_AROUND = Util.id("walk_around");
	public static final Identifier HARVEST = Util.id("harvest");
	public static final Identifier WAIT = Util.id("wait");
	public static final Identifier HOME = Util.id("home");
	public static final Identifier SET_HOME = Util.id("set_home");
	public static final Identifier SWITCH_TO_SLOT = Util.id("switch_to_slot");
	public static final Identifier WALK = Util.id("walk");
	public static final Identifier DROP = Util.id("drop");
	public static final Identifier SAY = Util.id("say");
	public static final Identifier USE = Util.id("use");
	public static final Identifier JUMP = Util.id("jump");
	public static final Identifier HIT_NEAREST_ENTITY = Util.id("hit_nearest_entity");
	public static final Identifier SET_YAW = Util.id("set_yaw");
	public static final Identifier SET_PITCH = Util.id("set_pitch");
	public static final Identifier BREAK_BLOCK = Util.id("break_block");

	public static void init() {
		Util.actionTypes(
			WALK_AROUND, new WalkAroundAction(),
			HARVEST, new HarvestAction(),
			WAIT, new WaitAction(),
			HOME, new HomeAction(),
			SET_HOME, new SetHomeAction(),
			SWITCH_TO_SLOT, new SwitchToSlotAction(),
			WALK, new WalkAction(),
			DROP, new DropAction(),
			SAY, new SayAction(),
			HIT_NEAREST_ENTITY, new HitNearestEntityAction(),
			SET_YAW, new SetYawAction(),
			SET_PITCH, new SetPitchAction(),
			BREAK_BLOCK, new BreakBlockAction(),
			USE, new UseAction(),
			JUMP, new JumpAction()
		);
	}
}
