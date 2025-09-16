package me.illia.robotmod.entity;

import me.illia.robotmod.Util;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class RobotEntityRenderer extends MobEntityRenderer<RobotEntity, RobotEntityModel> {
	public RobotEntityRenderer(EntityRendererFactory.Context context, RobotEntityModel entityModel, float f) {
		super(context, entityModel, f);
	}

	@Override
	public Identifier getTexture(LivingEntityRenderState state) {
		return Util.id("textures/entity/robot/robot.png");
	}

	@Override
	public EntityRenderState createRenderState() {
		return null;
	}
}
