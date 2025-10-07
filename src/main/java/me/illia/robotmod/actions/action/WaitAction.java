package me.illia.robotmod.actions.action;

import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.text.Text;

import java.util.List;

public class WaitAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
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
