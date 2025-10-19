package me.illia.robotmod.actions;

import me.illia.robotmod.entity.RobotEntity;
import net.minecraft.util.Identifier;

import java.util.List;

public class RepeatLastAction extends CustomAction {
	@Override
	public void run(RobotEntity robot) {
		if (robot.actionI <= 0) {
			return;
		}

		int targetI = robot.actionI - 1;

		while (targetI >= 0) {
			Action action = robot.actions.get(targetI);
			Identifier type = action.getActionType();

			if (type != ModActionTypes.REPEAT_LAST_ACTION) {
				ActionRunner.run(action, robot, targetI);
				return;
			}

			targetI--;
		}
	}

	@Override
	public List<ActionParamDescriptor> paramDescriptors() {
		return List.of();
	}

	@Override
	public String translation() {
		return "menu.robotmod.action_type_repeat_last_action";
	}
}
