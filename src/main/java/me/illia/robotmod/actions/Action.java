package me.illia.robotmod.actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Action {
	public static final Codec<HashMap<String, ParamValue>> PARAMS_CODEC = Codec.unboundedMap(Codec.STRING, ParamValue.CODEC)
		.xmap(
			HashMap::new,
			map -> map
		);

	public static final MapCodec<Action> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.fieldOf("action_type").forGetter((Action inst) -> inst.getActionType().getId()),
		PARAMS_CODEC.optionalFieldOf("params", new HashMap<>()).forGetter(Action::getParams)
	).apply(instance, (id, params) -> new Action(ActionType.from(id), params)));

	private static JsonElement toJsonElement(Object obj) {
		return switch (obj) {
			case Number number -> new JsonPrimitive(number);
			case Boolean bool -> new JsonPrimitive(bool);
			case String str -> new JsonPrimitive(str);
			default -> throw new IllegalArgumentException("Unsupported param type: " + obj.getClass().getName());
		};
	}

	public Action(ActionType actionType, HashMap<String, ParamValue> params) {
		this.actionType = actionType;
		this.params = params;
	}

	public Action(ActionType actionType) {
		this.actionType = actionType;
		this.params = new HashMap<>();
	}

	public ActionType actionType;
	public HashMap<String, ParamValue> params;
	public ActionType getActionType() {
		return actionType;
	}

	public int getActionTypeId() {
		return actionType.getId();
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public HashMap<String, ParamValue> getParams() {
		return params;
	}

	public void setParams(HashMap<String, ParamValue> params) {
		this.params = params;
	}

	public sealed interface ParamValue permits ParamValue.IntParam, ParamValue.FloatParam, ParamValue.BoolParam {
		Codec<ParamValue> CODEC = Codec.INT.<ParamValue>dispatch(
			ParamValue::getTypeTag,
			ParamValue::codecSelector
		);

		Codec<? extends ParamValue> codec();

		public static MapCodec<? extends ParamValue> codecSelector(int tag) {
			return switch (tag) {
				case 0 -> BoolParam.CODEC.fieldOf("value");
				case 1 -> FloatParam.CODEC.fieldOf("value");
				case 2 -> IntParam.CODEC.fieldOf("value");
				default -> throw new IllegalArgumentException("Invalid PV tag: " + tag);
			};
		}

		int getTypeTag();

		record IntParam(int value) implements ParamValue {
			public static final Codec<IntParam> CODEC = Codec.INT.xmap(IntParam::new, IntParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int getTypeTag() {
				return 0;
			}
		}

		record FloatParam(float value) implements ParamValue {
			public static final Codec<FloatParam> CODEC = Codec.FLOAT.xmap(FloatParam::new, FloatParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int getTypeTag() {
				return 1;
			}
		}

		record BoolParam(boolean value) implements ParamValue {
			public static final Codec<BoolParam> CODEC = Codec.BOOL.xmap(BoolParam::new, BoolParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int getTypeTag() {
				return 2;
			}
		}
	}
}