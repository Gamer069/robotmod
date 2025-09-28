package me.illia.robotmod.actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.illia.robotmod.Util;
import me.illia.robotmod.entity.RobotEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public void run(RobotEntity robotEntity) {
		List<ActionParamDescriptor> paramDescs = actionType.getParams();

		switch (actionType) {
			case WalkAround -> {
				ActionParamDescriptor radiusParamDesc = paramDescs.get(0);
				ParamValue val = params.get(Util.key(radiusParamDesc.name()));
				int r = 0;
				if (val instanceof ParamValue.IntParam(int value)) {
					r = value;
				} else {
					throw new RuntimeException("param isn't int for some reason");
				}

				EntityNavigation nav = robotEntity.getNavigation();

				BlockPos center = robotEntity.getBlockPos();
				int segments = 36;
				double speed = 1.0;

				List<Vec3d> path = new ArrayList<>();
				for (int i = 0; i < segments; i++) {
					double angle = 2 * Math.PI * i / segments;
					double x = center.getX() + 0.5 + r * Math.cos(angle);
					double z = center.getZ() + 0.5 + r * Math.sin(angle);
					double y = center.getY();
					path.add(new Vec3d(x, y, z));
				}
				path.add(new Vec3d(center.getX() + 0.5, center.getY(), center.getZ() + 0.5));

				int[] index = {0}; // tiny mutable holder

				robotEntity.getWorld().getServer().execute(() -> {
					ServerTickEvents.START_SERVER_TICK.register((minecraftServer -> {
						if (index[0] < path.size()) {
							Vec3d target = path.get(index[0]);
							if (robotEntity.squaredDistanceTo(target) < 1.0) {
								index[0]++; // next point
							} else {
								nav.startMovingTo(target.x, target.y, target.z, speed);
							}
						}
					}));
				});

			}
			case Harvest -> {
			}
			case Wait -> {
			}
			case Home -> {
			}
			case SetHome -> {
			}
		}
	}

	public sealed interface ParamValue permits ParamValue.IntParam, ParamValue.FloatParam, ParamValue.BoolParam {
		Codec<ParamValue> CODEC = Codec.INT.<ParamValue>dispatch(
			ParamValue::typeTag,
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
	}
}