package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.text.Text;

import java.util.List;

public class SetYawAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor yawParamDesc = paramDescriptors().get(0);
		Action.ParamValue yawVal = params.get(Util.key(yawParamDesc.name()));
		float yaw;
		if (yawVal instanceof Action.ParamValue.FloatParam(float value)) {
			yaw = value;
		} else {
			throw new RuntimeException("yaw isn't float for some reason, instead it's " + yawVal.type());
		}

		robot.setYaw(yaw);
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_yaw"), ActionParamType.Float));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_set_yaw";
	}
}
