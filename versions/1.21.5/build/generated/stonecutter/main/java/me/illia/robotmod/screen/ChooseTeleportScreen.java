package me.illia.robotmod.screen;

import me.illia.robotmod.Util;
import me.illia.robotmod.attachment.TeleportPoint;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.networking.RequestTeleportC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
//? if >=1.21.10 {
/*import net.minecraft.client.input.KeyInput;
*///?}
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ChooseTeleportScreen extends PlainHandledScreen<ChooseTeleportScreenHandler> {
	private GridWidget grid = new GridWidget().setColumnSpacing(5).setRowSpacing(5);
	private Random random;

	public ChooseTeleportScreen(ChooseTeleportScreenHandler handler, PlayerInventory playerInv, Text title) {
		super(handler, playerInv, title);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		grid.forEachChild(this::remove);

		grid = new GridWidget().setColumnSpacing(10).setRowSpacing(20);

		initGrid();

		super.resize(client, width, height);
	}

	public void handleTeleport(TeleportPoint point) {
		BlockPos pos = point.pos();

		client.player.playSound(SoundEvents.ENTITY_PLAYER_TELEPORT, 1.0f, 3.0f);

		client.player.getStackInHand(client.player.getActiveHand()).damage(1, client.player);

		World world = Util.entityWorld(client.player);

		ClientPlayNetworking.send(new RequestTeleportC2SPayload(pos, world.getRegistryKey()));

		client.player.closeScreen();

		client.player.sendMessage(Util.t("menu.robotmod.teleported", point.name()), true);
	}

	public void initGrid() {
		TeleportPointAttachedData points = handler.data;

		grid.getMainPositioner()
			.alignHorizontalCenter()
			.alignVerticalCenter()
			.margin(2);

		int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();

		GridWidget.Adder adder = grid.createAdder(scaledWidth / (100 + 5));

		for (TeleportPoint point : points.points()) {
			adder.add(ButtonWidget.builder(Text.literal(point.name()), button -> {
				handleTeleport(point);
			}).size(80, 20).build());
		}

		adder.add(ButtonWidget.builder(Util.t("menu.robotmod.random"), button -> {
			handleTeleport(points.points().get(random.nextBetween(0, points.points().size())));
		}).size(80, 20).build());

		grid.forEachChild(this::addDrawableChild);

		this.refreshWidgetPositions();
	}

	@Override
	protected void init() {
		this.random = Random.create(System.nanoTime());

		initGrid();
		super.init();
	}

	@Override
	protected void refreshWidgetPositions() {
		grid.refreshPositions();
	}

	@Override
	protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
	}

	@Override
	public boolean renderTitle() {
		return false;
	}

	//? if <1.21.10 {
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	//?} else {
/*
	@Override
	public boolean keyPressed(KeyInput input) {
		if (client.options.inventoryKey.matchesKey(input)) {
			close();
			return true;
		}
		return super.keyPressed(input);
	}
	*///?}
}
