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
import net.minecraft.world.World;
import me.illia.robotmod.item.TeleporterItem;

public class ChooseTeleportScreen extends PlainHandledScreen<ChooseTeleportScreenHandler> {
	private final GridWidget grid = new GridWidget().setColumnSpacing(10).setRowSpacing(20);

	public ChooseTeleportScreen(ChooseTeleportScreenHandler handler, PlayerInventory playerInv, Text title) {
		super(handler, playerInv, title);
	}

	@Override
	protected void init() {
		TeleportPointAttachedData points = handler.data;

		grid.getMainPositioner()
			.alignHorizontalCenter()
			.alignVerticalCenter()
			.margin(2)
			.marginY(2)
			.marginBottom(20);

		GridWidget.Adder adder = grid.createAdder(4);

		for (TeleportPoint point : points.points()) {
			adder.add(ButtonWidget.builder(Text.literal(point.name()), button -> {
				BlockPos pos = point.pos();
				ClientPlayerEntity player = MinecraftClient.getInstance().player;

				player.playSound(SoundEvents.ENTITY_PLAYER_TELEPORT, 1.0f, 3.0f);

				player.getStackInHand(player.getActiveHand()).damage(1, player);

				World world = Util.entityWorld(player);

				ClientPlayNetworking.send(new RequestTeleportC2SPayload(pos, world.getRegistryKey()));
				player.closeScreen();
			}).size(80, 20).build());
		}

		grid.forEachChild(this::addDrawableChild);

		this.grid.refreshPositions();
		this.refreshWidgetPositions();

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
