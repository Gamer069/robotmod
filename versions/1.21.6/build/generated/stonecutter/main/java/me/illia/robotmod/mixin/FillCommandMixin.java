package me.illia.robotmod.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.command.FillCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FillCommand.class)
public class FillCommandMixin {
	@Definition(id = "i", local = @Local(type = int.class, ordinal = 0))
	@Definition(id = "j", local = @Local(type = int.class, ordinal = 1))
	@Expression("i > j")
	@ModifyExpressionValue(method = "execute", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private static boolean execute(boolean original) {
		return false;
	}
}
