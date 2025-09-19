package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionType;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.HashMap;

public class RobotScreen extends HandledScreen<RobotScreenHandler> {
	public RobotScreen(RobotScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
//		this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.robotmod.add"), button -> {
		this.addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
			HashMap<String, Object> args = new HashMap<>();
			handler.actions.add(new Action(ActionType.Home, args));
		}).dimensions((width - backgroundWidth) / 2 + titleX, (height - backgroundHeight) / 2 + titleY + 20, 20, 20).build());

		this.addDrawableChild(new ActionsWidget((width - backgroundWidth) / 2 + titleX, (height - backgroundHeight) / 2 + titleY + 50, 90, 50, handler.getActions()));
		super.init();
	}

	@Override
	protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;

		context.drawTexture(RenderPipelines.GUI, Util.id("textures/gui/robot.png"), x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
	}
}
