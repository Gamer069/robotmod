package me.illia.robotmod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.illia.robotmod.screen.RobotScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HandledScreen.class)
public abstract class ScreenInventoryTextMixin {
	@Accessor("titleX")
	abstract int robotmod$getTitleX();

	@Accessor("titleY")
	abstract int robotmod$getTitleY();

	//? if > 1.21.2 {
	/*@WrapOperation(
		method = "renderMain",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/gui/DrawContext;II)V")
	)
	public void removeInventoryText(HandledScreen<?> instance, DrawContext context, int mouseX, int mouseY, Operation<Void> original) {
		if (!(instance instanceof RobotScreen screen)) {
			original.call(instance, context, mouseX, mouseY);
		} else {
			context.drawText(screen.getTextRenderer(), screen.getTitle(), robotmod$getTitleX(), robotmod$getTitleY(), Colors.DARK_GRAY, false);
		}
	}
	*///?} else {
	@WrapOperation(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/gui/DrawContext;II)V")
	)
	public void removeInventoryText(HandledScreen<?> instance, DrawContext context, int mouseX, int mouseY, Operation<Void> original) {
		if (!(instance instanceof RobotScreen screen)) {
			original.call(instance, context, mouseX, mouseY);
		} else {
			context.drawText(screen.getTextRenderer(), screen.getTitle(), robotmod$getTitleX(), robotmod$getTitleY(), -12566464, false);
		}
	}
	//?}
}
