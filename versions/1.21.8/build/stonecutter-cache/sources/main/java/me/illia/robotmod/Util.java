package me.illia.robotmod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionType;
import me.illia.robotmod.attachment.TeleportPoint;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.world.dimension.Dimension;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
//? if >= 1.21.8 {
import net.minecraft.client.data.*;
//?} else {
/*import net.minecraft.data.client.*;
*///?}
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	public static final Codec<TeleportPoint> TELEPORT_POINT_C = RecordCodecBuilder.create(inst -> inst.group(
		Codec.STRING.fieldOf("name").forGetter(TeleportPoint::name),
		BlockPos.CODEC.fieldOf("pos").forGetter(TeleportPoint::pos),
		World.CODEC.fieldOf("world").forGetter(TeleportPoint::world)
	).apply(inst, TeleportPoint::new));

	public static final Codec<TeleportPointAttachedData> TELEPORT_POINTS_C = RecordCodecBuilder.create(inst -> inst.group(
		TELEPORT_POINT_C.listOf().fieldOf("points").forGetter(TeleportPointAttachedData::points)
	).apply(inst, TeleportPointAttachedData::new));

	public static final PacketCodec<ByteBuf, TeleportPointAttachedData> TELEPORT_POINTS_PC = PacketCodecs.codec(TELEPORT_POINTS_C);

	public static Identifier id(String name) {
		return Identifier.of(Robotmod.MODID, name);
	}

	public static <T extends Entity> EntityType<T> entity(Identifier id, EntityType.Builder<T> type) {
		RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);

		return Registry.register(
			Registries.ENTITY_TYPE,
			key,
			type.build(key)
		);
	}

	public static Item item(Identifier id, Function<Item.Settings, Item> func, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return Registry.register(
			Registries.ITEM,
			key,
			func.apply(settings.registryKey(key))
		);
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> screenHandler(Identifier id, ScreenHandlerType.Factory<T> factory) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ScreenHandlerType<>(factory, FeatureSet.empty())
		);
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> screenHandler(Identifier id, ScreenHandlerType.Factory<T> factory, FeatureSet featureSet) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ScreenHandlerType<>(factory, featureSet)
		);
	}

	public static <T extends ScreenHandler, D> ExtendedScreenHandlerType<T, D> extendedScreenHandler(Identifier id, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<RegistryByteBuf, D> codec) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ExtendedScreenHandlerType<>(factory, codec)
		);
	}

	public static SpawnEggItem spawnEgg(Identifier id, BiFunction<EntityType<? extends MobEntity>, Item.Settings, Item> func, EntityType<? extends MobEntity> entity, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return (SpawnEggItem) Registry.register(
			Registries.ITEM,
			key,
			func.apply(entity, settings.registryKey(key))
		);
	}

	public static Text str(Action action) {
		// TODO: handle params and stuff
		return str(action.actionType);
	}

	public static Text str(ActionType actionType) {
		switch (actionType) {
			case WalkAround -> {
				return Text.translatable("menu.robotmod.action_type_walk_around");
			}
			case Harvest -> {
				return Text.translatable("menu.robotmod.action_type_harvest");
			}
			case Wait -> {
				return Text.translatable("menu.robotmod.action_type_wait");
			}
			case Home -> {
				return Text.translatable("menu.robotmod.action_type_home");
			}
		}

		return Text.empty();
	}

	public static Block block(Identifier id, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = blockFactory.apply(settings.registryKey(blockKey));

		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		BlockItem item = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
		Registry.register(Registries.ITEM, itemKey, item);

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static Block block(Identifier id, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, Rarity rarity) {
		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = blockFactory.apply(settings.registryKey(blockKey));

		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		BlockItem item = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey().rarity(rarity));
		Registry.register(Registries.ITEM, itemKey, item);

		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static Block blockWithoutItem(Identifier id, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = blockFactory.apply(settings.registryKey(blockKey));
		return Registry.register(Registries.BLOCK, blockKey, block);
	}

	public static ItemGroup itemGroup(Identifier id, String translationKey, ItemStack icon, Item... items) {
		RegistryKey<ItemGroup> groupKey = RegistryKey.of(RegistryKeys.ITEM_GROUP, id);
		ItemGroup group = Registry.register(Registries.ITEM_GROUP, groupKey, FabricItemGroup.builder().icon(() -> icon).displayName(Text.translatable(translationKey)).build());

		ItemGroupEvents.modifyEntriesEvent(groupKey).register(fabricItemGroupEntries -> {
			fabricItemGroupEntries.addAll(Arrays.stream(items).map(ItemStack::new).collect(Collectors.toSet()));
		});

		return group;
	}

	public static void itemModels(ItemModelGenerator gen, Item... items) {
		for (Item item : items) {
			gen.register(item, new Model(Optional.of(Util.mc("item/generated")), Optional.of("inventory"), TextureKey.LAYER0));
		}
	}

	public static Dimension dimKeys(Identifier id) {
		RegistryKey<DimensionOptions> options = RegistryKey.of(RegistryKeys.DIMENSION, id);
		RegistryKey<World> world = RegistryKey.of(RegistryKeys.WORLD, id);
		RegistryKey<DimensionType> type = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id.withSuffixedPath("_type"));

		return new Dimension(options, world, type);
	}

	public static void dim(Registerable<DimensionType> ctx, RegistryKey<DimensionType> type, Long fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, int coordinateScale, boolean bedWorks, boolean piglinSafe, int minY, int height, int logicalHeight, TagKey<Block> infiniburn, Identifier effectsLocation, float ambientLight, Optional<Integer> cloudHeight, DimensionType.MonsterSettings monsterSettings) {
		ctx.register(type, new DimensionType(
			fixedTime == null ? OptionalLong.empty() : OptionalLong.of(fixedTime),
			hasSkylight,
			hasCeiling,
			ultrawarm,
			natural,
			coordinateScale,
			bedWorks,
			piglinSafe,
			minY,
			height,
			logicalHeight,
			infiniburn,
			effectsLocation,
			ambientLight,
			//? if >= 1.21.8 {
			cloudHeight,
			//?}
			monsterSettings
		));
	}

	public static Identifier mc(String val) {
		return Identifier.ofVanilla(val);
	}
}