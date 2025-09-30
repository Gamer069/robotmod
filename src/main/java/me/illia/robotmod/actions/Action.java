package me.illia.robotmod.actions;

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
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
					throw new RuntimeException("param isn't int for some reason, instead it's " + val.type());
				}

				EntityNavigation nav = robotEntity.getNavigation();

				BlockPos center = robotEntity.getBlockPos();
				int segments = 72;
				double speed = 1.0;

				List<Vec3d> path = new ArrayList<>();
				for (int i = 0; i < segments; i++) {
					double angle = 2 * Math.PI * i / segments;
					double x = center.getX() + r * Math.sin(angle);
					double y = center.getY();
					double z = center.getZ() + r * Math.cos(angle);
					path.add(new Vec3d(x, y, z));
				}
				path.add(new Vec3d(center.getX(), center.getY(), center.getZ()));

				Robotmod.LOGGER.info("path: " + path.toString());

				AtomicInteger index = new AtomicInteger(0);

				ServerTickEvents.START_SERVER_TICK.register((minecraftServer -> {
					if (index.get() < path.size() && Util.night(robotEntity.getWorld())) {
						Vec3d target = path.get(index.get());
						if (!nav.isFollowingPath()) {
							nav.startMovingTo(target.x, target.y, target.z, speed);
							index.incrementAndGet(); // next point
						}
					}
				}));
			}
			case Harvest -> {
				if (robotEntity.getWorld().getBlockState(robotEntity.getBlockPos().up()).getBlock() instanceof CropBlock) {
					robotEntity.getWorld().breakBlock(robotEntity.getBlockPos().up(), true);
				}
			}
			case Wait -> {
			}
			case Home -> {
				BlockPos home = robotEntity.home;
				robotEntity.getNavigation().startMovingTo(home.getX(), home.getY(), home.getZ(), 1.0);
			}
			case SetHome -> {
				robotEntity.home = robotEntity.getBlockPos();
			}
			case SwitchToSlot -> {
				ActionParamDescriptor slotParamDesc = paramDescs.get(0);
				ParamValue val = params.get(Util.key(slotParamDesc.name()));
				int slot;
				if (val instanceof ParamValue.IntParam(int value)) {
					slot = value;
				} else {
					throw new RuntimeException("param isn't int for some reason, instead it's " + val.type());
				}

				robotEntity.slot = slot;
			}
			case Walk -> {
				ActionParamDescriptor walkParamDesc = paramDescs.get(0);
				ParamValue walkVal = params.get(Util.key(walkParamDesc.name()));
				Direction dir;
				if (walkVal instanceof ParamValue.DirParam(Direction dirValue)) {
					dir = dirValue;
				} else {
					throw new RuntimeException("param isn't dir for some reason, instead it's " + walkVal.type());
				}

				ActionParamDescriptor blocksParamDesc = paramDescs.get(1);
				ParamValue blocksVal = params.get(Util.key(blocksParamDesc.name()));
				int blocks;
				if (blocksVal instanceof ParamValue.IntParam(int blocksValue)) {
					blocks = blocksValue;
				} else {
					throw new RuntimeException("param isn't int for some reason, instead it's " + blocksVal.type());
				}

				net.minecraft.util.math.Direction mcDir = switch (dir) {
					case North -> net.minecraft.util.math.Direction.NORTH;
					case East -> net.minecraft.util.math.Direction.EAST;
					case South -> net.minecraft.util.math.Direction.SOUTH;
					case West -> net.minecraft.util.math.Direction.WEST;
				};

				BlockPos pos = robotEntity.getBlockPos().offset(mcDir, blocks);
				robotEntity.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), 1.0f);
			}
		}
	}

	public sealed interface ParamValue permits ParamValue.IntParam, ParamValue.FloatParam, ParamValue.BoolParam, ParamValue.DirParam {
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
	}
}
