package me.illia.robotmod;

import me.illia.robotmod.screen.dialog.AddActionsDialog;
import me.illia.robotmod.screen.dialog.AddActionsDialogHandler;
import net.minecraft.client.gui.screen.dialog.DialogBodyHandlers;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModDialogs {
	public static void init() {
		Registry.register(Registries.DIALOG_BODY_TYPE, Util.id("add_action"), AddActionsDialog.ADD_ACTIONS_DIALOG_CODEC);
		DialogBodyHandlers.register(AddActionsDialog.ADD_ACTIONS_DIALOG_CODEC, new AddActionsDialogHandler());
	}
}
