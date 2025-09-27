package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionType;
//? if = 1.21.8 {
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
//?} else {
/*import net.minecraft.client.render.RenderLayer;
*///?}
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.HashMap;

public class RobotScreen extends HandledScreen<RobotScreenHandler> {
	private CyclingButtonWidget<ActionType> actionTypeBtn;
	private ActionsWidget actionsWidget;

	public static final ActionType[] ACTION_TYPES = ActionType.values();

	public RobotScreen(RobotScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	public RobotEntity getRobot() {
		Entity entity = MinecraftClient.getInstance().world.getEntityById(handler.getEid());
		RobotEntity robotEntity = (RobotEntity)entity;

		if (robotEntity == null) {
			throw new RuntimeException("robot entity is null");
		}

		return robotEntity;
	}

	@Override
	protected void init() {
		int x = (width - backgroundWidth) / 2 + titleX;
		int y = (height - backgroundHeight) / 2 + titleY;

		actionTypeBtn = CyclingButtonWidget.<ActionType>builder(Util::str)
			.values(ACTION_TYPES)
			.build(x + 25, y + 20, 100, 20, Text.translatable("menu.robotmod.action_type"));
		this.addDrawableChild(actionTypeBtn);

		RobotEntity robot = getRobot();

		actionsWidget = new ActionsWidget(x, y + 50, 140, 50, robot.actions);
		this.addDrawableChild(actionsWidget);

		this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.robotmod.add"), button -> {
			HashMap<String, Action.ParamValue> args = new HashMap<>();
			robot.actions.add(new Action(actionTypeBtn.getValue(), args));

			this.remove(actionsWidget);
			actionsWidget = new ActionsWidget(x, y + 50, 90, 50, robot.actions);
			this.addDrawableChild(actionsWidget);
		}).dimensions(x, y + 20, 20, 20).build());

		super.init();
	}

	@Override
	public void close() {
		// WHAT DO I DO WITH THIS
		getRobot().save(actionsWidget.save());
		// ???
		super.close();
	}

	@Override
	protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;

		//? if = 1.21.8 {
		context.drawTexture(RenderPipelines.GUI, Util.id("textures/gui/robot.png"), x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		//?} else {
		/*context.drawTexture(RenderLayer::getGuiTextured, Util.id("textures/gui/robot.png"), x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
		*///?}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
		super.render(context, mouseX, mouseY, deltaTicks);
	}
}
