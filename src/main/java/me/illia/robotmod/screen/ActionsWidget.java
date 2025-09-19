package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

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
			context.drawText(renderer, action.getActionType().name(), getX(), getY() + (renderer.fontHeight + 10) * i, 0xFF00FF00, true);
			i++;
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		for (Action action : this.actions) {
			builder.put(NarrationPart.TITLE, Util.str(action));
		}
	}
}
