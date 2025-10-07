package me.illia.robotmod.registry;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.CustomAction;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ModRegistryKeys {
	public static final RegistryKey<Registry<CustomAction>> ACTION_TYPE = Util.key("action_type");
}
