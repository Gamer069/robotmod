package me.illia.robotmod.actions;

import java.util.HashMap;
import java.util.Map;

public enum ActionType {
	WalkAround(0),
	Harvest(1),
	Wait(2),
	Home(3);

	private static final Map<Integer, ActionType> BY_ID = new HashMap<>();

	static {
		for (ActionType type : values()) {
			BY_ID.put(type.id, type);
		}
	}

	public final int id;

	ActionType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ActionType from(int id) {
		return BY_ID.get(id);
	}
}
