package me.illia.robotmod.mixin;

import me.illia.robotmod.debug.ActionDebugRenderer;
import me.illia.robotmod.debug.DebugRenderers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//? if <1.21.10 {
/*@Mixin(DebugRenderer.class)
public class DebugRendererMixin implements DebugRenderers {
	@Unique
	private ActionDebugRenderer actionDebugRenderer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(MinecraftClient mc, CallbackInfo ci) {
		actionDebugRenderer = new ActionDebugRenderer();
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void render(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
		this.actionDebugRenderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
	}

	@Override
	public ActionDebugRenderer robotmod$getActionDebugRenderer() {
		return actionDebugRenderer;
	}

	@Override
	public void robotmod$setActionDebugRenderer(ActionDebugRenderer renderer) {
		actionDebugRenderer = renderer;
	}
}
*///?} else {
@Mixin(DebugRenderer.class)
public class DebugRendererMixin implements DebugRenderers {
	@Shadow
	@Final
	private List<DebugRenderer.Renderer> debugRenderers;
	@Unique
	private ActionDebugRenderer actionDebugRenderer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(CallbackInfo ci) {
		actionDebugRenderer = new ActionDebugRenderer();
		debugRenderers.add(actionDebugRenderer);
	}

	@Override
	public ActionDebugRenderer robotmod$getActionDebugRenderer() {
		return actionDebugRenderer;
	}

	@Override
	public void robotmod$setActionDebugRenderer(ActionDebugRenderer renderer) {
		actionDebugRenderer = renderer;
	}
}
//?}