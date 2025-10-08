package me.illia.robotmod.actions;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.illia.robotmod.Util;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Action {
	public static final Codec<HashMap<String, ParamValue>> PARAMS_CODEC = Codec.unboundedMap(Codec.STRING, ParamValue.CODEC)
		.xmap(
			HashMap::new,
			Function.identity()
		);

	public static final MapCodec<Action> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Identifier.CODEC.fieldOf("actionType").forGetter(Action::getActionType),
		PARAMS_CODEC.fieldOf("params").forGetter(Action::getParams)
	).apply(instance, Action::new));


	public Action(Identifier actionType, HashMap<String, ParamValue> params) {
		this.actionType = actionType;
		this.params = params;
	}

	public Action(Identifier actionType) {
		this.actionType = actionType;
		this.params = new HashMap<>();
	}

	public Identifier actionType;
	public HashMap<String, ParamValue> params;

	public Identifier getActionType() {
		return actionType;
	}

	public void setActionType(Identifier actionType) {
		this.actionType = actionType;
	}

	public HashMap<String, ParamValue> getParams() {
		return params;
	}

	public void setParams(HashMap<String, ParamValue> params) {
		this.params = params;
	}

	public sealed interface ParamValue permits ParamValue.IntParam, ParamValue.BoolParam, ParamValue.FloatParam, ParamValue.StringParam, ParamValue.DirParam {
		Codec<ParamValue> CODEC = Codec.INT.<ParamValue>dispatch(
			ParamValue::typeTag,
			ParamValue::codecSelector
		);

		Codec<? extends ParamValue> codec();

		public static MapCodec<? extends ParamValue> codecSelector(int tag) {
			return switch (tag) {
				case 0 -> IntParam.CODEC.fieldOf("value");
				case 1 -> FloatParam.CODEC.fieldOf("value");
				case 2 -> BoolParam.CODEC.fieldOf("value");
				case 3 -> DirParam.CODEC.fieldOf("value");
				case 4 -> StringParam.CODEC.fieldOf("value");
				default -> throw new IllegalArgumentException("Invalid PV tag: " + tag);
			};
		}

		public static String val(ParamValue val) {
			if (val == null) return "";

			return switch (val) {
				case IntParam(int value) -> Integer.toString(value);
				case FloatParam(float value) -> Float.toString(value);
				case BoolParam(boolean value) -> Util.t("bool.robotmod." + value).getString();
				case StringParam(String value) -> value;
				case DirParam(Direction dir) -> dir.asString();
			};
		}

		int typeTag();
		ActionParamType type();

		record IntParam(int value) implements ParamValue {
			public static final Codec<IntParam> CODEC = Codec.INT.xmap(IntParam::new, IntParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int typeTag() {
				return 0;
			}

			@Override
			public ActionParamType type() {
				return ActionParamType.Int;
			}
		}

		record FloatParam(float value) implements ParamValue {
			public static final Codec<FloatParam> CODEC = Codec.FLOAT.xmap(FloatParam::new, FloatParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int typeTag() {
				return 1;
			}

			@Override
			public ActionParamType type() {
				return ActionParamType.Float;
			}
		}

		record BoolParam(boolean value) implements ParamValue {
			public static final Codec<BoolParam> CODEC = Codec.BOOL.xmap(BoolParam::new, BoolParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int typeTag() {
				return 2;
			}

			@Override
			public ActionParamType type() {
				return ActionParamType.Bool;
			}
		}

		record DirParam(Direction dir) implements ParamValue {
			public static final Codec<DirParam> CODEC = Direction.CODEC.xmap(DirParam::new, DirParam::dir);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int typeTag() {
				return 3;
			}

			@Override
			public ActionParamType type() {
				return ActionParamType.Dir;
			}
		}

		public record StringParam(String value) implements ParamValue {
			public static final Codec<StringParam> CODEC = Codec.STRING.xmap(StringParam::new, StringParam::value);

			@Override
			public Codec<? extends ParamValue> codec() {
				return CODEC;
			}

			@Override
			public int typeTag() {
				return 4;
			}

			@Override
			public ActionParamType type() {
				return ActionParamType.String;
			}
		}
	}
}
