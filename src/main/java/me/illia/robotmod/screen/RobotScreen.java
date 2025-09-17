package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class RobotScreen extends HandledScreen<RobotScreenHandler> {
	public RobotScreen(RobotScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;

		context.drawTexture(RenderPipelines.GUI, Util.id("textures/gui/robot.png"), x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
	}
}
