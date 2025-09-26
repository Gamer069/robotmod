package me.illia.robotmod.item;

import me.illia.robotmod.attachment.ModAttachmentTypes;
import me.illia.robotmod.attachment.TeleportPointAttachedData;
import me.illia.robotmod.screen.ChooseTeleportScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TeleporterItem extends Item {
	public TeleporterItem(Settings settings) {
		super(settings);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			// TODO: nope
			ServerWorld serverWorld = (ServerWorld)world;
			if (serverWorld.getAttachedOrElse(ModAttachmentTypes.TELEPORT_POINTS, TeleportPointAttachedData.DEFAULT).points().isEmpty()) {
				user.sendMessage(Text.translatable("menu.robotmod.no_points").styled(s -> s.withColor(TextColor.fromRgb(0xFF0000))), true);
			} else {
				MinecraftClient.getInstance().setScreen(new ChooseTeleportScreen(serverWorld.getAttachedOrElse(ModAttachmentTypes.TELEPORT_POINTS, TeleportPointAttachedData.DEFAULT)));
			}
		}

		return super.use(world, user, hand);
	}
}
