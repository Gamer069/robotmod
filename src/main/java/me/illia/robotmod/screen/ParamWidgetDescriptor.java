package me.illia.robotmod.screen;

import me.illia.robotmod.actions.ActionParamDescriptor;
import net.minecraft.client.gui.widget.ClickableWidget;

public record ParamWidgetDescriptor(ClickableWidget widget, ActionParamDescriptor desc, int actionI) {
}
