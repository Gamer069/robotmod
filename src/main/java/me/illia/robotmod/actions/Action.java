package me.illia.robotmod.actions;

import com.ibm.icu.util.CharsTrie;
import me.illia.robotmod.Robotmod;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.RobotEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Action {
	public static final Codec<HashMap<String, ParamValue>> PARAMS_CODEC = Codec.unboundedMap(Codec.STRING, ParamValue.CODEC)
		.xmap(
			HashMap::new,
			Function.identity()
		);

	public static final MapCodec<Action> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.INT.fieldOf("actionType").forGetter((Action inst) -> inst.getActionType().getId()),
		PARAMS_CODEC.fieldOf("params").forGetter(Action::getParams)
	).apply(instance, (id, params) -> new Action(ActionType.from(id), params)));


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

	public sealed interface ParamValue permits ParamValue.BoolParam, ParamValue.DirParam, ParamValue.FloatParam, ParamValue.IntParam, ParamValue.StringParam {
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
			if (val == null) {
				return "";
			}

			switch (val) {
				case IntParam(int value) -> {
					return Integer.toString(value);
				}
				case FloatParam(float value) -> {
					return Float.toString(value);
				}
				case BoolParam(boolean value) -> {
					return Text.translatable("bool.robotmod." + value).getString();
				}
				case StringParam(String value) -> {
					return value;
				}
				case DirParam(Direction dir) -> {
					return dir.asString();
				}
			}
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
