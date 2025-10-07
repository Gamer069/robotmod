package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.text.Text;

import java.util.List;

public class SwitchToSlotAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor slotDesc = paramDescriptors().get(0);
		Action.ParamValue val = params.get(Util.key(slotDesc.name()));
		int slot;
		if (val instanceof Action.ParamValue.IntParam(int value)) {
			slot = value;
		} else {
			throw new RuntimeException("unexpected type: expected int, got " + val.type());
		}

		robot.slot = slot;
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_switch_slot"), ActionParamType.Int));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_switch_to_slot";
	}
}
