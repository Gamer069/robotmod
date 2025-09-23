package me.illia.robotmod.attachment;

import me.illia.robotmod.Robotmod;
import me.illia.robotmod.Util;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import java.util.ArrayList;

public class ModAttachmentTypes {
	@SuppressWarnings("UnstableApiUsage")
	public static final AttachmentType<TeleportPointAttachedData> TELEPORT_POINTS = AttachmentRegistry.create(
		Util.id("teleport_points"),
		builder -> builder
			.initializer(() -> TeleportPointAttachedData.DEFAULT)
			.persistent(Util.TELEPORT_POINTS_C)
			.syncWith(
				Util.TELEPORT_POINTS_PC,
				AttachmentSyncPredicate.all()
			)
	);

	public static void init() {
		Robotmod.LOGGER.info("initializing attachment types for " + Robotmod.MODID);
	}
}
