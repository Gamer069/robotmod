package me.illia.robotmod.entity;

//? if 1.21.3 {
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

public class RobotEntityRenderState extends LivingEntityRenderState {
//?} else {
/*import net.minecraft.client.render.entity.state.ArmedEntityRenderState;

public class RobotEntityRenderState extends ArmedEntityRenderState {
*///?}
	public float headPitch = 0;
	public float headYaw = 0;
}
