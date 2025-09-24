package me.illia.robotmod.attachment;

import me.illia.robotmod.Robotmod;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record TeleportPointAttachedData(List<TeleportPoint> points) {
	public static TeleportPointAttachedData DEFAULT = new TeleportPointAttachedData(List.of());

	public TeleportPointAttachedData addPoint(TeleportPoint point) {
		ArrayList<TeleportPoint> points2 = new ArrayList<>(points);
		points2.add(point);
		return new TeleportPointAttachedData(List.copyOf(points2));
	}

	public TeleportPointAttachedData removePoint(TeleportPoint point) {
		if (!points.contains(point)) return this;

		ArrayList<TeleportPoint> points2 = new ArrayList<>(points);
		points2.removeIf(v -> v == point);

		return new TeleportPointAttachedData(points2);
	}

	public TeleportPointAttachedData removePointByPos(BlockPos pos) {
		Optional<TeleportPoint> found = findPointByPos(pos);
		if (found.isEmpty()) return this;

		ArrayList<TeleportPoint> points2 = new ArrayList<>(points);
		points2.removeIf(v -> v == found.get());

		return new TeleportPointAttachedData(points2);
	}

	public TeleportPointAttachedData clear() {
		return DEFAULT;
	}

	public Optional<TeleportPoint> findPointByPos(BlockPos pos) {
		return points.stream()
			.filter(element -> element.pos().equals(pos))
			.findFirst();
	}
}
