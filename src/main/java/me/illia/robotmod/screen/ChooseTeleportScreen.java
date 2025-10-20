package me.illia.robotmod.screen;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import me.illia.robotmod.attachment.TeleportPoint;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.networking.GetTeleportPointsC2SPayload;
import me.illia.robotmod.networking.RequestTeleportC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
//? if >=1.21.10 {
import net.minecraft.client.input.KeyInput;
//?}
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import me.illia.robotmod.item.TeleporterItem;

public class ChooseTeleportScreen extends Screen {
	public TeleportPointAttachedData points;

	public ChooseTeleportScreen(TeleportPointAttachedData points) {
		super(Util.t("menu.robotmod.tp"));
		this.points = points;
	}

	@Override
	protected void init() {
		// If client doesn't have data yet, request it from the server and wait for the response.
		// This avoids showing stale/empty lists when the client hasn't fetched the authoritative attachment.
		if (TeleportPointAttachedData.DATA == null) {
			Robotmod.LOGGER.info("teleport point attached data data == null");
			if (MinecraftClient.getInstance().world != null) {
				Robotmod.LOGGER.info("sending packet");
				ClientPlayNetworking.send(new GetTeleportPointsC2SPayload(MinecraftClient.getInstance().world.getRegistryKey()));
				TeleporterItem.sentPacket = true;
				Robotmod.LOGGER.info("sent packet = true");
			}
			// don't populate UI yet; it will be recreated when the packet arrives
			return;
		}

		// Use the freshest client data
		this.points = TeleportPointAttachedData.DATA;

		int i = 0;
		for (TeleportPoint point : points.points()) {
			this.addDrawableChild(ButtonWidget.builder(Text.literal(point.name()), button -> {
				BlockPos pos = point.pos();
				ClientPlayerEntity player = MinecraftClient.getInstance().player;

				player.playSound(SoundEvents.ENTITY_PLAYER_TELEPORT, 1.0f, 3.0f);

				player.getStackInHand(player.getActiveHand()).damage(1, player);

				World world = Util.entityWorld(player);

				ClientPlayNetworking.send(new RequestTeleportC2SPayload(pos, world.getRegistryKey()));
				player.closeScreen();
			}).dimensions(100, 20 + 30 * i, 100, 20).build());
			i++;
		}

		super.init();
	}

	@Override
	public void close() {
		TeleportPointAttachedData.DATA = null;
		super.close();
	}

	//? if <1.21.10 {
	/*@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	*///?} else {

	@Override
	public boolean keyPressed(KeyInput input) {
		// failsafe for something that will never happen
		if (client == null) client = MinecraftClient.getInstance();

		if (client.options.inventoryKey.matchesKey(input)) {
			close();
			return true;
		}
		return super.keyPressed(input);
	}
	//?}
}
