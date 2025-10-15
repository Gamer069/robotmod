package me.illia.robotmod.actions.action;

import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionParamDescriptor;
import me.illia.robotmod.actions.ActionParamType;
import me.illia.robotmod.actions.CustomAction;
import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class SayAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		ActionParamDescriptor msgParamDesc = paramDescriptors().get(0);
		Action.ParamValue msgVal = params.get(Util.key(msgParamDesc.name()));
		String msg;
		if (msgVal instanceof Action.ParamValue.StringParam(String value)) {
			msg = value;
		} else {
			throw new RuntimeException("msg isn't string for some reason, instead it's " + msgVal.type());
		}

		Text text = robot.getName().copy().append(" > ").append(msg);

		World world = Util.entityWorld(robot);
		for (PlayerEntity player : world.getPlayers()) {
			player.sendMessage(text, false);
		}
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of(new ActionParamDescriptor(Text.translatable("menu.robotmod.action_param_say"), ActionParamType.String));
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_say";
	}
}
