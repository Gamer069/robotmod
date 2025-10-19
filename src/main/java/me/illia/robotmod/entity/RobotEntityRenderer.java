package me.illia.robotmod.entity;

import me.illia.robotmod.Util;
import me.illia.robotmod.client.RobotmodClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
//? if >1.21.3 {
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemDisplayContext;
//?}
import net.minecraft.util.Identifier;

import java.awt.*;

public class RobotEntityRenderer extends MobEntityRenderer<RobotEntity, RobotEntityRenderState, RobotEntityModel> {
	//? if >1.21.3 {
	public ItemModelManager itemModelManager;
	//?}

	public RobotEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new RobotEntityModel(context.getPart(RobotmodClient.MODEL_ROBOT_LAYER)), 0.5f);

		//? if >1.21.3 {
		this.addFeature(new HeldItemFeatureRenderer<>(this));
		itemModelManager = context.getItemModelManager();
		//?} else {
		/*this.addFeature(new HeldItemFeatureRenderer<>(this, context.getItemRenderer()));
		*///?}
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
		super.updateRenderState(livingEntity, livingEntityRenderState, f);

		livingEntityRenderState.headPitch = livingEntity.getHeadPitch();
		livingEntityRenderState.headYaw = livingEntity.getHeadYaw();

		//? if >1.21.3 {
		itemModelManager.updateForLivingEntity(livingEntityRenderState.rightHandItemState, livingEntity.inv.getStack(livingEntity.slot), ItemDisplayContext.FIXED, livingEntity);
		//?} else {
		/*livingEntityRenderState.rightHandStack = livingEntity.inv.getStack(livingEntity.slot);
		*///?}
	}
}