package me.illia.robotmod.actions;

import net.minecraft.text.Text;

import java.util.*;

public enum ActionType {
	WalkAround(0),
	Harvest(1),
	Wait(2),
	Home(3),
	SetHome(4);

	private static final Map<Integer, ActionType> BY_ID = new HashMap<>();
	public static final Map<Integer, List<ActionParamDescriptor>> ID2PARAMS = new HashMap<>();

	static {
		for (ActionType type : values()) {
			BY_ID.put(type.id, type);
			switch (type.id) {
				case 0:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_walk_around"), ActionParamType.Int)));
					break;
				case 2:
					ID2PARAMS.put(type.id, List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_wait_sec"), ActionParamType.Float)));
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
