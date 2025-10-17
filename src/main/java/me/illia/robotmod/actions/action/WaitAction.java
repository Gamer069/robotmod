package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.*;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.text.Text;

import java.util.List;

public class WaitAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor desc = paramDescriptors().get(0);
		Action.ParamValue param = params.get(Util.key(desc.name()));
		if (!(param instanceof Action.ParamValue.FloatParam(float val))) {
			return;
		}

		ActionRunner.stopFor(robot, Math.round(val * 20f));
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_wait_sec"), ActionParamType.Float));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_wait";
	}
}
