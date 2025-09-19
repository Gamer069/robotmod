package me.illia.robotmod;

import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Util {
	public static final PacketCodec<? super RegistryByteBuf, Integer> INT_PC = new PacketCodec<>() {
		@Override
		public void encode(RegistryByteBuf buf, Integer value) {
			buf.writeInt(value);
		}

		@Override
		public Integer decode(RegistryByteBuf buf) {
			return buf.readInt();
		}
	};
	public static final PacketCodec<RegistryByteBuf, ArrayList<Action>> ACTIONS_PC = new PacketCodec<>() {
		@Override
		public void encode(RegistryByteBuf buf, ArrayList<Action> actions) {
			buf.writeVarInt(actions.size());
			for (Action action : actions) {
				// Write action_type (int ID)
				buf.writeVarInt(action.getActionType().getId());

				// Write params
				HashMap<String, Object> params = action.getParams();
				buf.writeVarInt(params.size());

				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String key = entry.getKey();
					Object value = entry.getValue();

					buf.writeString(key);

					if (value instanceof Integer) {
						buf.writeByte(0); // tag
						buf.writeVarInt((int) value);
					} else if (value instanceof Double) {
						buf.writeByte(1);
						buf.writeDouble((double) value);
					} else if (value instanceof Boolean) {
						buf.writeByte(2);
						buf.writeBoolean((boolean) value);
					} else if (value instanceof String) {
						buf.writeByte(3);
						buf.writeString((String) value);
					} else {
						throw new IllegalArgumentException("Unsupported param type: " + value.getClass().getName());
					}
				}
			}
		}

		@Override
		public ArrayList<Action> decode(RegistryByteBuf buf) {
			int size = buf.readVarInt();
			ArrayList<Action> actions = new ArrayList<>(size);

			for (int i = 0; i < size; i++) {
				int typeId = buf.readVarInt();
				ActionType type = ActionType.from(typeId);

				int paramCount = buf.readVarInt();
				HashMap<String, Object> params = new HashMap<>(paramCount);

				for (int j = 0; j < paramCount; j++) {
					String key = buf.readString();
					byte tag = buf.readByte();

					Object value = switch (tag) {
						case 0 -> buf.readVarInt();
						case 1 -> buf.readDouble();
						case 2 -> buf.readBoolean();
						case 3 -> buf.readString();
						default -> throw new IllegalArgumentException("Unknown param tag: " + tag);
					};

					params.put(key, value);
				}

				actions.add(new Action(type, params));
			}

			return actions;
		}
	};

	public static Identifier id(String name) {
		return Identifier.of(Robotmod.MODID, name);
	}

	public static <T extends Entity> EntityType<T> regEntity(Identifier id, EntityType.Builder<T> type) {
		RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);

		return Registry.register(
			Registries.ENTITY_TYPE,
			key,
			type.build(key)
		);
	}

	public static Item regItem(Identifier id, Function<Item.Settings, Item> func, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return Registry.register(
			Registries.ITEM,
			key,
			func.apply(settings.registryKey(key))
		);
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> regScreenHandler(Identifier id, ScreenHandlerType.Factory<T> factory) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ScreenHandlerType<>(factory, FeatureSet.empty())
		);
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> regScreenHandler(Identifier id, ScreenHandlerType.Factory<T> factory, FeatureSet featureSet) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ScreenHandlerType<>(factory, featureSet)
		);
	}

	public static <T extends ScreenHandler, D> ExtendedScreenHandlerType<T, D> regExtendedScreenHandler(Identifier id, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<RegistryByteBuf, D> codec) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ExtendedScreenHandlerType<>(factory, codec)
		);
	}

	public static SpawnEggItem regSpawnEggItem(Identifier id, BiFunction<EntityType<? extends MobEntity>, Item.Settings, Item> func, EntityType<? extends MobEntity> entity, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return (SpawnEggItem) Registry.register(
			Registries.ITEM,
			key,
			func.apply(entity, settings.registryKey(key))
		);
	}

	public static String str(Action action) {
		String actionName = "";
		switch (action.actionType) {
			case WalkAround -> {
				actionName = "Walk around";
			}
			case Harvest -> {
				actionName = "Harvest";
			}
			case Wait -> {
				actionName = "Wait";
			}
			case Home -> {
				actionName = "Home";
			}
		}
		return actionName;
	}
}