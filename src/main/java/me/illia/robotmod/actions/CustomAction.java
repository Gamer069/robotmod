package me.illia.robotmod.actions;

import me.illia.robotmod.entity.RobotEntity;

import java.util.HashMap;
import java.util.List;

public abstract class CustomAction {
	public HashMap<String, Action.ParamValue> params;

	public CustomAction(HashMap<String, Action.ParamValue> params) {
		this.params = params;
	}

	public CustomAction() {
		this.params = new HashMap<>();
	}

	public CustomAction(CustomAction customAction) {
		this.params = customAction.params;
	}

	public abstract void run(RobotEntity robot);
	public abstract List<ActionParamDescriptor> paramDescriptors();
	public abstract String translation();
}