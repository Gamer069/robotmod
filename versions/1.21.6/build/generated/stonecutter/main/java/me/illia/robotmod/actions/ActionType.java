package me.illia.robotmod.actions;

import net.minecraft.text.Text;

import java.util.*;

public enum ActionType {
	WalkAround(0),
	Harvest(1),
	Wait(2),
	Home(3),
	SetHome(4),
	SwitchToSlot(5),
	Walk(6),
	Drop(7),
	Say(8),
	HitNearestEntity(9),
	SetYaw(10),
	SetPitch(11),
	BreakBlock(12);

	private static final Map<Integer, ActionType> BY_ID = new HashMap<>();
	public static final Map<Integer, List<ActionParamDescriptor>> ID2PARAMS = new HashMap<>();

	static {
		for (ActionType type : values()) {
			BY_ID.put(type.id, type);
			switch (type.id) {
				case 0:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_walk_around"), ActionParamType.Int)));
					break;
				case 2:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_wait_sec"), ActionParamType.Float)));
					break;
				case 5:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_switch_slot"), ActionParamType.Int)));
					break;
				case 6:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_walk_to"), ActionParamType.Dir), new ActionParamDescriptor(Util.t("menu.robotmod.action_param_walk_blocks"), ActionParamType.Int)));
					break;
				case 8:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_say"), ActionParamType.String)));
					break;
				case 10:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_yaw"), ActionParamType.Float)));
					break;
				case 11:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_pitch"), ActionParamType.Float)));
					break;
				case 12:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Util.t("menu.robotmod.action_param_break_fluid"), ActionParamType.Bool)));
					break;
				default:
					ID2PARAMS.put(type.id, List.of());
					break;
			}
		}
	}

	public final int id;

	ActionType(int id) {
		this.id = id;
	}

	public static Collection<ActionType> getTypes() {
		return BY_ID.values();
	}

	public int getId() {
		return id;
	}

	public List<ActionParamDescriptor> getParams() {
		return ID2PARAMS.get(id);
	}

	public static ActionType from(int id) {
		return BY_ID.get(id);
	}
}
