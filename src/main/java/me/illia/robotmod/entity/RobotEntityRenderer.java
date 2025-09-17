package me.illia.robotmod.entity;

import me.illia.robotmod.Util;
import me.illia.robotmod.client.RobotmodClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class RobotEntityRenderer extends MobEntityRenderer<RobotEntity, LivingEntityRenderState, RobotEntityModel> {
	public RobotEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RobotEntityModel(context.getPart(RobotmodClient.MODEL_ROBOT_LAYER)), 0.5f);
	}

	@Override
	public Identifier getTexture(LivingEntityRenderState state) {
		return Util.id("textures/entity/robot/robot.png");
	}

	@Override
	public LivingEntityRenderState createRenderState() {
		return new LivingEntityRenderState();
	}
}
