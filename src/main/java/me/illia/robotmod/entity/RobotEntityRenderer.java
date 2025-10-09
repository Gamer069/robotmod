package me.illia.robotmod.entity;

import me.illia.robotmod.Util;
import me.illia.robotmod.client.RobotmodClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;

public class RobotEntityRenderer extends MobEntityRenderer<RobotEntity, RobotEntityRenderState, RobotEntityModel> {
	public RobotEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RobotEntityModel(context.getPart(RobotmodClient.MODEL_ROBOT_LAYER)), 0.5f);

		//? if != 1.21.3 {
		/*this.addFeature(new HeldItemFeatureRenderer<>(this));
		*///?} else {
		this.addFeature(new HeldItemFeatureRenderer<>(this, context.getItemRenderer()));
		//?}
	}

	@Override
	public Identifier getTexture(RobotEntityRenderState state) {
		return Util.id("textures/entity/robot/robot.png");
	}

	@Override
	public RobotEntityRenderState createRenderState() {
		return new RobotEntityRenderState();
	}

	@Override
	public void updateRenderState(RobotEntity livingEntity, RobotEntityRenderState livingEntityRenderState, float f) {
		livingEntityRenderState.heldStack = livingEntity.inv.getStack(livingEntity.slot);
		super.updateRenderState(livingEntity, livingEntityRenderState, f);
	}
}
