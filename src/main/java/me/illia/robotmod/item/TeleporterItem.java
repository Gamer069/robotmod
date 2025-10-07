package me.illia.robotmod.item;

import me.illia.robotmod.Util;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.networking.GetTeleportPointsC2SPayload;
import me.illia.robotmod.screen.ChooseTeleportScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TeleporterItem extends Item {
	public static boolean sentPacket = false;

	public TeleporterItem(Settings settings) {
		super(settings);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			if (TeleportPointAttachedData.DATA == null && !sentPacket) {
				ClientPlayNetworking.send(new GetTeleportPointsC2SPayload(world.getRegistryKey()));
				sentPacket = true;
			} else if (TeleportPointAttachedData.DATA != null && sentPacket) {
				if (TeleportPointAttachedData.DATA.points().isEmpty()) {
					user.sendMessage(Util.t("menu.robotmod.no_points").styled(s -> s.withColor(TextColor.fromRgb(0xFF0000))), true);
				} else {
					MinecraftClient.getInstance().setScreen(new ChooseTeleportScreen(TeleportPointAttachedData.DATA));
				}
			}
		}

		return super.use(world, user, hand);
	}
}
