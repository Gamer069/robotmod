package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.text.Text;

import java.util.List;

public class SetPitchAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor pitchParamDesc = paramDescriptors().get(0);
		Action.ParamValue pitchVal = params.get(Util.key(pitchParamDesc.name()));
		float pitch;
		if (pitchVal instanceof Action.ParamValue.FloatParam(float value)) {
			pitch = value;
		} else {
			throw new RuntimeException("pitch isn't float for some reason, instead it's " + pitchVal.type());
		}

		robot.setPitch(pitch);
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_pitch"), ActionParamType.Float));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_set_pitch";
	}
}
