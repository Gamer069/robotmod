package me.illia.robotmod.registry;

import me.illia.robotmod.actions.CustomAction;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;

public class ModRegistries {
	public static final Registry<CustomAction> ACTION_TYPE = FabricRegistryBuilder.createSimple(ModRegistryKeys.ACTION_TYPE).attribute(RegistryAttribute.SYNCED).buildAndRegister();
}
