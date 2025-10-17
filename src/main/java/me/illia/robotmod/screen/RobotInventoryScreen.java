package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
//? if >=1.21.5 {
/*import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.gl.RenderPipelines;
*///? }
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import net.minecraft.client.render.RenderLayer;

public class RobotInventoryScreen extends HandledScreen<RobotInventoryScreenHandler> {
	public RobotInventoryScreen(RobotInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);

		backgroundWidth = 175;
		backgroundHeight = 138;
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;

		//? if >= 1.21.6 {
		/*context.drawTexture(RenderPipelines.GUI_TEXTURED, Util.id("textures/gui/robot_inv.png"), x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		 *///?} else {
		context.drawTexture(RenderLayer::getGuiTextured, Util.id("textures/gui/robot_inv.png"), x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		//?}
	}
}
