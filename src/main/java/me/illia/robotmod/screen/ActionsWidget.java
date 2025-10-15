package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.*;
import me.illia.robotmod.registry.ModRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
//? if >=1.21.10 {
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
//?}
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ActionsWidget extends ClickableWidget {
	private final ArrayList<Action> actions;
	private final List<ParamWidgetDescriptor> paramWidgets = new ArrayList<>();

	public ActionsWidget(int x, int y, int w, int h, ArrayList<Action> actions) {
		super(x, y, w, h, Util.t("menu.robotmod.actions"));
		this.actions = actions;
		initParamWidgets();
	}

	public ArrayList<Action> save() {
		for (ParamWidgetDescriptor desc : paramWidgets) {
			Action action = actions.get(desc.actionI());
			ActionParamDescriptor paramDesc = desc.desc();
			ClickableWidget widget = desc.widget();

			String paramName = Util.key(paramDesc.name());
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
				case String -> {
					if (widget instanceof TextFieldWidget tf) {
						yield new Action.ParamValue.StringParam(tf.getText());
					} else {
						yield null;
					}
				}
				case Bool -> {
					if (widget instanceof CyclingButtonWidget<?> cb && cb.getValue() instanceof Boolean val) {
						yield new Action.ParamValue.BoolParam(val);
					} else {
						yield null;
					}
				}
				case Dir -> {
					if (widget instanceof CyclingButtonWidget<?> cb && cb.getValue() instanceof Direction val) {
						yield new Action.ParamValue.DirParam(val);
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

	//? if <1.21.10 {
	/*@Override
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
	*///?} else {
	@Override
	public boolean charTyped(CharInput input) {
		for (ParamWidgetDescriptor w : paramWidgets) {
			if (w.widget() instanceof TextFieldWidget tf && tf.isFocused()) tf.setText(tf.getText() + input.asString());
		}
		return super.charTyped(input);
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		boolean handled = false;
		double mouseX = click.x();
		double mouseY = click.y();
		int button = click.button();
		for (ParamWidgetDescriptor widget : paramWidgets) {
			if (widget.widget() instanceof TextFieldWidget tf) {
				if (tf.isMouseOver(mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					for (ParamWidgetDescriptor w : paramWidgets) {
						w.widget().setFocused(false);
					}
					tf.setFocused(true);
				}
			}
			handled |= widget.widget().mouseClicked(click, doubled);
		}
		return handled || super.mouseClicked(click, doubled);
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		boolean handled = false;
		for (ParamWidgetDescriptor w : paramWidgets) {
			if (w.widget() instanceof TextFieldWidget tf) handled |= tf.keyPressed(input);
		};
		return handled || super.keyPressed(input);
	}

	//?}

	private void initParamWidgets() {
		TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
		int yOffset = 0;
		int actionI = 0;

		for (Action action : actions) {
			String actionTxt = Util.str(action).getString();
			int actionTxtW = renderer.getWidth(actionTxt);
			int y = getY() + (renderer.fontHeight + 10) * actionI;

			int i = 0;
			CustomAction customAction = ModRegistries.ACTION_TYPE.get(action.actionType);
			for (ActionParamDescriptor desc : customAction.paramDescriptors()) {
				i++;
				int widgetX = getX() + actionTxtW + 60 * (i - 1);
				int paramLabelX = getX() + actionTxtW + 60 * i;

				switch (desc.type()) {
					case Int -> {
						TextFieldWidget widget = new TextFieldWidget(renderer, widgetX, y, 60, 20, desc.name());
						widget.setTextPredicate(s -> {
							try {
								Integer.parseInt(s);
								return true;
							} catch (NumberFormatException _e) {
								return s.isEmpty();
							}
						});
						TranslatableTextContent content = (TranslatableTextContent)desc.name().getContent();
						Action.ParamValue val = action.getParams().get(content.getKey());
						if (val instanceof Action.ParamValue.IntParam(int value)) {
							widget.setText(Integer.toString(value));
						}

						paramWidgets.add(new ParamWidgetDescriptor(widget, desc, actionI, paramLabelX));
					}
					case Float -> {
						TextFieldWidget widget = new TextFieldWidget(renderer, widgetX, y, 60, 20, desc.name());
						widget.setTextPredicate(s -> {
							if (s.isEmpty() || s.equals("-") || s.equals(".") || s.equals("-.")) return true;
							try {
								Float.parseFloat(s);
								return true;
							} catch (NumberFormatException _e) {
								return false;
							}
						});

						TranslatableTextContent content = (TranslatableTextContent)desc.name().getContent();
						Action.ParamValue val = action.getParams().get(content.getKey());
						if (val instanceof Action.ParamValue.FloatParam(float value)) {
							widget.setText(Float.toString(value));
						}

						paramWidgets.add(new ParamWidgetDescriptor(widget, desc, actionI, paramLabelX));
					}
					case String -> {
						TextFieldWidget widget = new TextFieldWidget(renderer, widgetX, y, 60, 20, desc.name());

						TranslatableTextContent content = (TranslatableTextContent)desc.name().getContent();
						Action.ParamValue val = action.getParams().get(content.getKey());
						if (val instanceof Action.ParamValue.StringParam(String value)) {
							widget.setText(value);
						}

						paramWidgets.add(new ParamWidgetDescriptor(widget, desc, actionI, paramLabelX));
					}
					case Bool -> {
						CyclingButtonWidget<Boolean> boolWidget = CyclingButtonWidget.onOffBuilder()
							.omitKeyText()
							.build(widgetX, y, 60, 20, desc.name());

						TranslatableTextContent content = (TranslatableTextContent)desc.name().getContent();
						Action.ParamValue val = action.getParams().get(content.getKey());
						if (val instanceof Action.ParamValue.BoolParam(boolean value)) {
							boolWidget.setValue(value);
						}

						paramWidgets.add(new ParamWidgetDescriptor(boolWidget, desc, actionI, paramLabelX));
					}
					case Dir -> {
						CyclingButtonWidget<Direction> dirWidget = CyclingButtonWidget.<Direction>builder((d) -> Util.t(d.asString()))
							.values(Direction.values())
							.initially(Direction.North)
							.omitKeyText()
							.build(widgetX, y, 60, 20, desc.name());

						TranslatableTextContent content = (TranslatableTextContent)desc.name().getContent();
						Action.ParamValue val = action.getParams().get(content.getKey());
						if (val instanceof Action.ParamValue.DirParam(Direction value)) {
							dirWidget.setValue(value);
						}


						paramWidgets.add(new ParamWidgetDescriptor(dirWidget, desc, actionI, paramLabelX));
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
		//? if <1.21.10 {
		/*context.drawBorder(getX(), getY(), this.width, this.height, 0xFFFF0000);
		*///?} else {
		context.drawStrokedRectangle(getX(), getY(), this.width, this.height, 0xFFFF0000);
		//?}

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
			setWidth(Math.max(getWidth(), renderer.getWidth(widget.desc().name().getString())));
			int actionH = (renderer.fontHeight + 10) * widget.actionI();
			setHeight(Math.max(getHeight(), actionH + widget.widget().getHeight()));

			String paramName = widget.desc().name().getString();
			context.drawText(
				renderer,
				paramName,
				widget.paramLabelX(),
				getY() + actionH,
				0xFF0000FF,
				true
			);

			setWidth(Math.max(getWidth(), widget.widget().getWidth() + renderer.getWidth(widget.desc().name().getString())));

			widget.widget().render(context, mouseX, mouseY, deltaTicks);
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
		for (Action action : this.actions) {
			builder.put(NarrationPart.TITLE, Util.str(action).getString());
			CustomAction customAction = ModRegistries.ACTION_TYPE.get(action.actionType);
			for (ActionParamDescriptor desc : customAction.paramDescriptors()) {
				builder.put(NarrationPart.HINT, desc.name().getString());
				Action.ParamValue value = action.getParams().get(((TranslatableTextContent)desc.name().getContent()).getKey());
				builder.put(NarrationPart.HINT, Action.ParamValue.val(value));
			}
		}
	}
}
