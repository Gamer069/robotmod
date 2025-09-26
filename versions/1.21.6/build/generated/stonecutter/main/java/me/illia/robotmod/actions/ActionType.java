package me.illia.robotmod.actions;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ActionType {
	WalkAround(0),
	Harvest(1),
	Wait(2),
	Home(3),
	SetHome(4);

	private static final Map<Integer, ActionType> BY_ID = new HashMap<>();
	private static final Map<Integer, ArrayList<ActionParamDescriptor>> ID2PARAMS = new HashMap<>();

	static {
		for (ActionType type : values()) {
			BY_ID.put(type.id, type);
			switch (type) {
				case WalkAround:
					ID2PARAMS.put(type.id, (ArrayList<ActionParamDescriptor>)List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_walk_around"), ActionParamType.Int)));
				case Wait:
					ID2PARAMS.put(type.id, (ArrayList<ActionParamDescriptor>)List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_wait_sec"), ActionParamType.Float)));
				default:
					ID2PARAMS.put(type.id, new ArrayList<>());
			}
		}
	}

	public final int id;

	ActionType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public ArrayList<ActionParamDescriptor> getParams() {
		return ID2PARAMS.get(id);
	}

	public static ActionType from(int id) {
		return BY_ID.get(id);
	}
}
