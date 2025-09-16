package me.illia.robotmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Util {
	public static Identifier id(String name) {
		return Identifier.of(Robotmod.MODID, name);
	}

	public static<T extends Entity> EntityType<T> regEntity(Identifier id, EntityType.Builder<T> type) {
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
			func.apply(settings)
		);
	}

	public static SpawnEggItem regSpawnEggItem(Identifier id, BiFunction<EntityType<? extends MobEntity>, Item.Settings, Item> func, EntityType<? extends MobEntity> entity, Item.Settings settings) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

		return (SpawnEggItem)Registry.register(
			Registries.ITEM,
			key,
			func.apply(entity, settings)
		);
	}
}
