package me.illia.robotmod.screen;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActionsWidget extends ClickableWidget {
	private final ArrayList<Action> actions;
	private final List<ClickableWidget> paramWidgets = new ArrayList<>();

	public ActionsWidget(int x, int y, int w, int h, ArrayList<Action> actions) {
		super(x, y, w, h, Text.translatable("menu.robotmod.actions"));
		this.actions = actions;
		initParamWidgets();
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		for (ClickableWidget widget : paramWidgets) {
			if (widget instanceof TextFieldWidget tf) {
				tf.charTyped(chr, modifiers);
			}
		}
		return super.charTyped(chr, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean handled = false;
		for (ClickableWidget widget : paramWidgets) {
			handled |= widget.mouseClicked(mouseX, mouseY, button);
		}
		return handled || super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean handled = false;
		for (ClickableWidget w : paramWidgets) {
			if (w instanceof TextFieldWidget tf) handled |= tf.keyPressed(keyCode, scanCode, modifiers);
		}
		return handled || super.keyPressed(keyCode, scanCode, modifiers);
	}

	private void initParamWidgets() {
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		int yOffset = 0;

		for (Action action : actions) {
			String actionTxt = Util.str(action).getString();
			int actionTxtW = renderer.getWidth(actionTxt);

			for (ActionParamDescriptor desc : action.actionType.getParams()) {
				int widgetX = getX() + actionTxtW + 20;
				int widgetY = getY() + yOffset;

				switch (desc.type()) {
					case Int -> {
						TextFieldWidget widget = new TextFieldWidget(renderer, widgetX, widgetY, 60, 20, desc.name());
						widget.setTextPredicate(s -> {
							try {
								Integer.parseInt(s);
								return true;
							} catch (NumberFormatException _e) {
								return false;
							}
						});
						paramWidgets.add(widget);
					}
					case Float -> {
						TextFieldWidget widget = new TextFieldWidget(renderer, widgetX, widgetY, 60, 20, desc.name());
						widget.setTextPredicate(s -> {
							if (s.isEmpty() || s.equals("-") || s.equals(".") || s.equals("-.")) return true;
							try {
								Float.parseFloat(s);
								return true;
							} catch (NumberFormatException _e) {
								return false;
							}
						});
						paramWidgets.add(widget);
					}
					case Boolean -> {
						CyclingButtonWidget<Boolean> boolWidget = CyclingButtonWidget.onOffBuilder()
							.build(widgetX, widgetY, 60, 20, desc.name());
						paramWidgets.add(boolWidget);
					}
				}

				yOffset += 25; // spacing between widgets
			}

			yOffset += 10; // spacing between actions
		}
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		context.drawBorder(getX(), getY(), this.width, this.height, 0xFFFF0000);

		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		int i = 0;

		for (Action action : actions) {
			String actionTxt = Util.str(action).getString();
			int y = getY() + (renderer.fontHeight + 10) * i;

			context.fill(getX(), y, getX() + renderer.getWidth(actionTxt) + action.getParams().size() * 30, y, 0xFF0000FF);
			context.drawText(renderer, actionTxt, getX(), y, 0xFF00FF00, true);
			i++;
		}

		// Render all param widgets
		for (ClickableWidget widget : paramWidgets) {
			widget.renderWidget(context, mouseX, mouseY, deltaTicks);
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		for (Action action : this.actions) {
			builder.put(NarrationPart.TITLE, Util.str(action).getString());
		}
	}
}
