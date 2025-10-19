package me.illia.robotmod.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.illia.robotmod.debug.ActionDebugRenderer;
import me.illia.robotmod.debug.DebugRenderers;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
//? if >=1.21.10 {
import net.minecraft.client.input.KeyInput;
//?}
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Keyboard.class)
public abstract class DebugMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	protected abstract void debugLog(Text text);

	@ModifyReturnValue(method = "processF3", at = @At("TAIL"))
	//? if <1.21.10 {
	/*public boolean processF3(boolean original, int key) {
	*///?} else {
	public boolean processF3(boolean original, KeyInput input) {
		int key = input.key();
	//?}
		if (key == InputUtil.GLFW_KEY_R) {
			//? if <1.21.10 {
			/*DebugRenderers renderers = (DebugRenderers)(client.debugRenderer);
			*///?} else {
			DebugRenderers renderers = (DebugRenderers)(client.worldRenderer.debugRenderer);
			//?}
			ActionDebugRenderer actionDebugRenderer = renderers.robotmod$getActionDebugRenderer();

			actionDebugRenderer.enabled = !actionDebugRenderer.enabled;

			debugLog(Text.literal("Action renderer: " + (actionDebugRenderer.enabled ? "ON" : "OFF")));

			return true;
		}
		return original;
	}
}
