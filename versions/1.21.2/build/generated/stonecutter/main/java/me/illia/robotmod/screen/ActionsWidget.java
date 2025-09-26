package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.dialog.input.TextInputControl;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;

public class ActionsWidget extends ClickableWidget {
	public ArrayList<Action> actions;

	public ActionsWidget(int x, int y, int w, int h, ArrayList<Action> actions) {
		super(x, y, w, h, Text.literal("actions"));
		this.actions = actions;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		context.drawBorder(getX(), getY(), this.width, this.height, 0xFFFF0000);

		int i = 0;
		for (Action action : actions) {
			TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

			String actionTxt = Util.str(action).getString();
			int y = getY() + (renderer.fontHeight + 10) * i;
			int actionTxtW = renderer.getWidth(actionTxt);

			context.fill(getX(), y, getX() + actionTxtW + action.getParams().size() * 30, y, 0xFF0000FF);

			context.drawText(renderer, actionTxt, getX(), y, 0xFF00FF00, true);

			for (ActionParamDescriptor desc : action.actionType.getParams()) {
				context.drawText(renderer, desc.name(), getX() + actionTxtW + 20, y, 0xFF0000FF, true);

				switch (desc.type()) {
					case Int, Float -> {
						EditBoxWidget widget = new EditBoxWidget.Builder().x(getX() + renderer.getWidth(desc.name()) + 25).y(y).build(renderer, renderer.getWidth(desc.name()) + 10, 20, desc.name());
						widget.renderWidget(context, mouseX, mouseY, deltaTicks);
					}
					case Boolean -> {
						CyclingButtonWidget<Boolean> boolWidget = CyclingButtonWidget.onOffBuilder().build(getX(), y, renderer.getWidth(desc.name()) + 5, 20, desc.name());
						boolWidget.render(context, mouseX, mouseY, deltaTicks);
					}
				}
			}

			i++;
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		for (Action action : this.actions) {
			builder.put(NarrationPart.TITLE, Util.str(action).getString());
		}
	}
}
