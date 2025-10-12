package me.illia.robotmod.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.illia.robotmod.debug.ActionDebugRenderer;
import me.illia.robotmod.debug.DebugRenderers;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(Keyboard.class)
public abstract class DebugMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	protected abstract void debugLog(Text text);

	@ModifyReturnValue(method = "processF3", at = @At("TAIL"))
	public boolean processF3(boolean original, int key) {
		if (key == InputUtil.GLFW_KEY_R) {
			DebugRenderers renderers = (DebugRenderers)client.debugRenderer;
			ActionDebugRenderer actionDebugRenderer = renderers.robotmod$getActionDebugRenderer();

			actionDebugRenderer.enabled = !actionDebugRenderer.enabled;

			debugLog(Text.literal("Action renderer: " + (actionDebugRenderer.enabled ? "ON" : "OFF")));

			return true;
		}
		return original;
	}
}
