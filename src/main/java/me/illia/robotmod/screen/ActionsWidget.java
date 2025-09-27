package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActionsWidget extends ClickableWidget {
	private final ArrayList<Action> actions;
	private final List<ParamWidgetDescriptor> paramWidgets = new ArrayList<>();

	public ActionsWidget(int x, int y, int w, int h, ArrayList<Action> actions) {
		super(x, y, w, h, Text.translatable("menu.robotmod.actions"));
		this.actions = actions;
		initParamWidgets();
	}

	public ArrayList<Action> save() {
		for (ParamWidgetDescriptor desc : paramWidgets) {
			Action action = actions.get(desc.actionI());
			ActionParamDescriptor paramDesc = desc.desc();
			ClickableWidget widget = desc.widget();

			String paramName = paramDesc.name().getString();
			Action.ParamValue paramValue = switch (paramDesc.type()) {
				case Int -> {
					if (widget instanceof TextFieldWidget tf) {
						try {
							yield new Action.ParamValue.IntParam(Integer.parseInt(tf.getText()));
						} catch (NumberFormatException e) {
							yield new Action.ParamValue.IntParam(0);
						}
					} else {
						yield null;
					}
				}
				case Float -> {
					if (widget instanceof TextFieldWidget tf) {
						try {
							yield new Action.ParamValue.FloatParam(Float.parseFloat(tf.getText()));
						} catch (NumberFormatException e) {
							yield new Action.ParamValue.FloatParam(0);
						}
					} else {
						yield null;
					}
				}
				case Boolean -> {
					if (widget instanceof CyclingButtonWidget<?> cb && cb.getValue() instanceof Boolean val) {
						yield new Action.ParamValue.BoolParam(val);
					} else {
						yield null;
					}
				}
			};

			if (paramValue != null) {
				action.getParams().put(paramName, paramValue);
			}
		}

		return actions;
	}

	@Override
	public boolean charTyped(char chr, int modifiers) {
		for (ParamWidgetDescriptor w : paramWidgets) {
			if (w.widget() instanceof TextFieldWidget tf && tf.isFocused()) tf.setText(tf.getText() + chr);
		}
		return super.charTyped(chr, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean handled = false;
		for (ParamWidgetDescriptor widget : paramWidgets) {
			if (widget.widget() instanceof TextFieldWidget tf) {
				if (tf.isMouseOver(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					for (ParamWidgetDescriptor w : paramWidgets) {
						w.widget().setFocused(false);
					}
					tf.setFocused(true);
				}
			}
			handled |= widget.widget().mouseClicked(mouseX, mouseY, button);
		}
		return handled || super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		boolean handled = false;
		for (ParamWidgetDescriptor w : paramWidgets) {
			if (w.widget() instanceof TextFieldWidget tf) handled |= tf.keyPressed(keyCode, scanCode, modifiers);
		};
		return handled || super.keyPressed(keyCode, scanCode, modifiers);
	}

	private void initParamWidgets() {
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		int yOffset = 0;
		int actionI = 0;

		for (Action action : actions) {
			String actionTxt = Util.str(action).getString();
			int actionTxtW = renderer.getWidth(actionTxt);

			int i = 0;
			for (ActionParamDescriptor desc : action.actionType.getParams()) {
				i++;
				int widgetX = getX() + actionTxtW + 20;
				int widgetY = getY() + yOffset + 20 * i;

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
						paramWidgets.add(new ParamWidgetDescriptor(widget, desc, actionI));
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
						paramWidgets.add(new ParamWidgetDescriptor(widget, desc, actionI));
					}
					case Boolean -> {
						CyclingButtonWidget<Boolean> boolWidget = CyclingButtonWidget.onOffBuilder()
							.build(widgetX, widgetY, 60, 20, desc.name());
						paramWidgets.add(new ParamWidgetDescriptor(boolWidget, desc, actionI));
					}
				}

				yOffset += 25; // spacing between widgets
			}

			yOffset += 10; // spacing between actions
			actionI++;
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
		for (ParamWidgetDescriptor widget : paramWidgets) {
			widget.widget().render(context, mouseX, mouseY, deltaTicks);
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		for (Action action : this.actions) {
			builder.put(NarrationPart.TITLE, Util.str(action).getString());
		}
	}
}
