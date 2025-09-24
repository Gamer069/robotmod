package me.illia.robotmod.actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Action {
	public static final Codec<HashMap<String, Object>> PARAMS_CODEC = Codec.unboundedMap(Codec.STRING, Codec.PASSTHROUGH)
		.xmap(
			// Map<String, Dynamic<?>> → HashMap<String, Object>
			(Map<String, Dynamic<?>> dynamicMap) -> {
				HashMap<String, Object> result = new HashMap<>();
				for (Map.Entry<String, Dynamic<?>> entry : dynamicMap.entrySet()) {
					Object value = entry.getValue().getValue();
					result.put(entry.getKey(), value);
				}
				return result;
			},
			// HashMap<String, Object> → Map<String, Dynamic<?>>
			(HashMap<String, Object> objectMap) -> {
				return objectMap.entrySet().stream().collect(Collectors.toMap(
					Map.Entry::getKey,
					entry -> new Dynamic<>(JsonOps.INSTANCE, toJsonElement(entry.getValue()))
				));
			}
		);

	public static final MapCodec<Action> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.fieldOf("action_type").forGetter((Action inst) -> inst.getActionType().getId()),
		PARAMS_CODEC.optionalFieldOf("params", new HashMap<>()).forGetter(Action::getParams)
	).apply(instance, (id, params) -> new Action(ActionType.from(id), params)));

	private static JsonElement toJsonElement(Object obj) {
		if (obj instanceof Number number) {
			return new JsonPrimitive(number);
		} else if (obj instanceof Boolean bool) {
			return new JsonPrimitive(bool);
		} else if (obj instanceof String str) {
			return new JsonPrimitive(str);
		} else {
			throw new IllegalArgumentException("Unsupported param type: " + obj.getClass().getName());
		}
	}

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
