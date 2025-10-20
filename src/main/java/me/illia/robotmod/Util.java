package me.illia.robotmod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.actions.Direction;
import me.illia.robotmod.attachment.TeleportPoint;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.registry.ModRegistries;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
//? if >= 1.21.5 {
/*import net.minecraft.client.data.*;
*///?} else {
import net.minecraft.client.gui.DrawContext;
import net.minecraft.data.client.*;
//?}
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelTransform;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.io.function.IOQuadFunction;
import net.minecraft.block.BlockState;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {
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

	public static<T> RegistryKey<Registry<T>> key(String id) {
		return RegistryKey.ofRegistry(id(id));
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

	public static <T extends ScreenHandler, D> ExtendedScreenHandlerType<T, D> extendedScreenHandler(Identifier id, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<ByteBuf, D> codec) {
		RegistryKey<ScreenHandlerType<?>> key = RegistryKey.of(RegistryKeys.SCREEN_HANDLER, id);
		return Registry.register(
			Registries.SCREEN_HANDLER,
			key,
			new ExtendedScreenHandlerType<>(factory, codec)
		);
	}

	//? if <1.21.10 {
	public static SpawnEggItem spawnEgg(Identifier id, BiFunction<EntityType<? extends MobEntity>, Item.Settings, Item> func, EntityType<? extends MobEntity> entity, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return (SpawnEggItem) Registry.register(
			Registries.ITEM,
			key,
			func.apply(entity, settings.registryKey(key))
		);
	}
	//?} else {
	/*public static SpawnEggItem spawnEgg(Identifier id, Function<Item.Settings, Item> func, EntityType<? extends MobEntity> entity, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return (SpawnEggItem) Registry.register(
			Registries.ITEM,
			key,
			func.apply(settings.registryKey(key).spawnEgg(entity))
		);
	}
	*///?}

	public static SpawnEggItem spawnEgg(Identifier id, IOQuadFunction<EntityType<? extends MobEntity>, Integer, Integer, Item.Settings, SpawnEggItem> func, EntityType<? extends MobEntity> entity, int primaryColor, int secondaryColor, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		try {
			return Registry.register(
				Registries.ITEM,
				key,
				func.apply(entity, primaryColor, secondaryColor, settings.registryKey(key))
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Text str(Action action) {
		// TODO: handle params and stuff
		return str(action.actionType);
	}

	public static Text str(Identifier actionType) {
		CustomAction action = ModRegistries.ACTION_TYPE.get(actionType);
		return Util.t(action.translation());
	}

	public static MutableText t(String key) {
		return Text.translatable(key);
	}

	public static MutableText t(String key, Object... args) {
		return Text.translatable(key, args);
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
		ItemGroup group = Registry.register(Registries.ITEM_GROUP, groupKey, FabricItemGroup.builder().icon(() -> icon).displayName(Util.t(translationKey)).build());

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

	public static Identifier mc(String val) {
		return Identifier.ofVanilla(val);
	}

	public static ModelTransform pivot(float x, float y, float z) {
		//? if >= 1.21.5 {
		/*return ModelTransform.origin(x, y, z);
		*///?} else {
		return ModelTransform.pivot(x, y, z);
		//?}
	}

	public static boolean night(World world) {
		long timeOfDay = world.getTimeOfDay() % 24000L;
		return timeOfDay >= 13000L && timeOfDay <= 23000L;
	}

	public static String key(Text name) {
		if (name.getContent() instanceof TranslatableTextContent ttc) {
			return ttc.getKey();
		}
		return null;
	}

	public static void add(FabricLanguageProvider.TranslationBuilder builder, String key, String string) {
		builder.add(key, string);
	}

	public static void add(FabricLanguageProvider.TranslationBuilder builder, Identifier type, String string) {
		builder.add(((TranslatableTextContent)str(type).getContent()).getKey(), string);
	}

	public static void add(FabricLanguageProvider.TranslationBuilder builder, Item key, String string) {
		builder.add(key, string);
	}

	public static void add(FabricLanguageProvider.TranslationBuilder builder, Block key, String string) {
		builder.add(key, string);
	}

	public static void add(FabricLanguageProvider.TranslationBuilder builder, EntityType<?> key, String string) {
		builder.add(key, string);
	}

	public static boolean nearest(Entity entity, int r, Function<BlockState, Boolean> func) {
		return circleFilled(entity.getBlockPos(), r).stream().map(cPos -> entityWorld(entity).getBlockState(cPos)).anyMatch(func::apply);
	}

	public static List<BlockPos> circleFilled(BlockPos center, int radius) {
		List<BlockPos> list = new ArrayList<>();
		int cx = center.getX(), cz = center.getZ(), cy = center.getY();
		int r2 = radius * radius;
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					if (dx*dx + dz*dz <= r2) {
						list.add(new BlockPos(cx + dx, cy + dy, cz + dz));
					}
				}
			}
		}
		return list;
	}

	public static void actionType(String key, CustomAction action) {
		Registry.register(ModRegistries.ACTION_TYPE, id(key), action);
	}

	public static void actionType(Identifier key, CustomAction action) {
		Registry.register(ModRegistries.ACTION_TYPE, key, action);
	}

	public static void actionTypes(Object... idsAndActions) {
		if (idsAndActions.length % 2 != 0)
			throw new IllegalArgumentException("You must provide pairs of Identifier and CustomAction");

		for (int i = 0; i < idsAndActions.length; i += 2) {
			Identifier id = (Identifier) idsAndActions[i];
			CustomAction action = (CustomAction) idsAndActions[i + 1];
			Registry.register(ModRegistries.ACTION_TYPE, id, action);
		}
	}

	public static World entityWorld(Entity entity) {
		//? if <1.21.10 {
		return entity.getWorld();
		//?} else {
		/*return entity.getEntityWorld();
		*///?}
	}

	public static Vec3d entityPos(Entity entity) {
		//? if <1.21.10 {
		return entity.getPos();
		//?} else {
		/*return entity.getEntityPos();
		 *///?}
	}

	public static ServerWorld serverEntityWorld(Entity entity) {
		//? if <1.21.10 {
		return (ServerWorld)entity.getWorld();
		 //?} else {
		/*return (ServerWorld)entity.getEntityWorld();
		*///?}
	}

	public static Text e() {
		return Text.empty();
	}

	public static void border(DrawContext ctx, int x, int y, int w, int h, int col) {
		//? if <1.21.10 {
		ctx.drawBorder(x, y, w, h, col);
		//? } else {
		/*ctx.drawStrokedRectangle(x, y, w, h, col);
		*///? }
	}
}
