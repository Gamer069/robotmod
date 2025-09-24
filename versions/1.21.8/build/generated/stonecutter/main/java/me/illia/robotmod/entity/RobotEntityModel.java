package me.illia.robotmod.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

import java.util.function.Function;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class RobotEntityModel extends EntityModel<LivingEntityRenderState> {
	private final ModelPart head;
	private final ModelPart legs;
	private final ModelPart bb_main;

	public RobotEntityModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.legs = root.getChild("legs");
		this.bb_main = root.getChild("bb_main");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -6.0F, -2.0F, 10.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 12.0F, 0.0F));

		ModelPartData antenna_r1 = head.addChild("antenna_r1", ModelPartBuilder.create().uv(8, 26).cuboid(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -6.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData antenna_r2 = head.addChild("antenna_r2", ModelPartBuilder.create().uv(22, 21).cuboid(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.75F, -8.5F, 0.0F, 0.0F, 0.0F, -0.6981F));

		ModelPartData antenna_r3 = head.addChild("antenna_r3", ModelPartBuilder.create().uv(12, 21).cuboid(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.75F, -7.75F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData antenna_r4 = head.addChild("antenna_r4", ModelPartBuilder.create().uv(24, 16).cuboid(-1.0F, -3.0F, -1.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -5.25F, 0.0F, 0.0F, 0.0F, 0.6981F));

		ModelPartData legs = modelPartData.addChild("legs", ModelPartBuilder.create().uv(24, 10).cuboid(1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 26).cuboid(-3.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));

		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 10).cuboid(-4.0F, -11.0F, -2.0F, 8.0F, 7.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 21).cuboid(-1.0F, -12.0F, -2.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.origin(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public void setAngles(LivingEntityRenderState state) {
		super.setAngles(state);
	}
}