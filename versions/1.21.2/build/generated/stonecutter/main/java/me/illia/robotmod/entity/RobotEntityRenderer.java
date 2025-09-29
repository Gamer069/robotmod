package me.illia.robotmod.entity;

import me.illia.robotmod.Util;
import me.illia.robotmod.client.RobotmodClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class RobotEntityRenderer extends MobEntityRenderer<RobotEntity, ArmedEntityRenderState, RobotEntityModel> {
	public RobotEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RobotEntityModel(context.getPart(RobotmodClient.MODEL_ROBOT_LAYER)), 0.5f);

		this.addFeature(new HeldItemFeatureRenderer<>(this));
	}

	@Override
	public Identifier getTexture(ArmedEntityRenderState state) {
		return Util.id("textures/entity/robot/robot.png");
	}

	@Override
	public ArmedEntityRenderState createRenderState() {
		return new ArmedEntityRenderState();
	}
}
