
package me.illia.robotmod.screen;

import me.illia.robotmod.attachment.TeleportPoint;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.networking.RequestTeleportC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ChooseTeleportScreen extends Screen {
	public TeleportPointAttachedData points;

	public ChooseTeleportScreen(TeleportPointAttachedData points) {
		super(Text.translatable("menu.robotmod.tp"));
		this.points = points;
	}

	@Override
	protected void init() {
		int i = 0;
		for (TeleportPoint point : points.points()) {
			this.addDrawableChild(ButtonWidget.builder(Text.literal(point.name()), button -> {
				BlockPos pos = point.pos();
				ClientPlayerEntity player = MinecraftClient.getInstance().player;

				player.playSound(SoundEvents.ENTITY_PLAYER_TELEPORT, 1.0f, 3.0f);

				player.getStackInHand(player.getActiveHand()).damage(1, player);

				ClientPlayNetworking.send(new RequestTeleportC2SPayload(pos, player.getWorld().getRegistryKey()));
				player.closeScreen();
			}).dimensions(100, 20 + 30 * i, 100, 20).build());
			i++;
		}

		super.init();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
