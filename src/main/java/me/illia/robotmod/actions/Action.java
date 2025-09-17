package me.illia.robotmod.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;

public class Action {
	public static final MapCodec<Action> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.fieldOf("action_type").forGetter((inst) -> ((Action)inst).getActionType().getId())
	).apply(instance, (id) -> new Action(ActionType.from(id))));

	public Action(ActionType actionType, HashMap<String, Object> params) {
		this.actionType = actionType;
		this.params = params;
	}

	public Action(ActionType actionType) {
		this.actionType = actionType;
		this.params = new HashMap<>();
	}

	public ActionType actionType;
	public HashMap<String, Object> params;

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
}
