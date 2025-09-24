package me.illia.robotmod.screen.dialog;

import net.minecraft.client.gui.screen.dialog.DialogBodyHandler;
import net.minecraft.client.gui.screen.dialog.DialogScreen;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.gui.widget.Widget;

public class AddActionsDialogHandler implements DialogBodyHandler<AddActionsDialog> {
	@Override
	public Widget createWidget(DialogScreen<?> dialogScreen, AddActionsDialog body) {
		return new ToggleButtonWidget(0, 0, 90, 90, true);
	}
}
