package me.illia.robotmod.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.screen.RobotScreenHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;

import java.util.ArrayList;
import java.util.List;

public class RobotEntity extends PathAwareEntity implements SmartBrainOwner<RobotEntity> {
	public ArrayList<Action> actions;

	public RobotEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.actions = new ArrayList<>();
	}

	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);
	}

	@Override
	protected void writeCustomData(WriteView view) {
		WriteView list = view.getList("actions").add();

		for (Action action : actions) {
			list.put("action", Action.CODEC.codec(), action);
		}

		super.writeCustomData(view);
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!this.getWorld().isClient && !player.isSneaking()) {
			player.openHandledScreen(new NamedScreenHandlerFactory() {
				@Override
				public Text getDisplayName() {
					return Text.translatable("menu.robotmod.robot");
				}

				@Override
				public RobotScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new RobotScreenHandler(syncId, RobotEntity.this.getId());
				}
			});
		}
		return ActionResult.SUCCESS;
	}

	@Override
	protected Brain.Profile<?> createBrainProfile() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void mobTick(ServerWorld world) {
		tickBrain(this);
		super.mobTick(world);
	}

	@Override
	public List<ExtendedSensor<RobotEntity>> getSensors() {
		return ObjectArrayList.of(
			new HurtBySensor<>(),
			new NearbyLivingEntitySensor<>()
		);
	}

	@Override
	public BrainActivityGroup<RobotEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
			new LookAtTarget<>(),
			new MoveToWalkTarget<>().cooldownForBetween(20, 60)
		);
	}

	@Override
	public BrainActivityGroup<RobotEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
			new SetPlayerLookTarget<>(),
			new MoveOutsideWater<>()
		);
	}

	@Override
	public boolean hurtByWater() {
		return true;
	}

	@Override
	public void tick() {
		if (!this.getWorld().isClient)
			tickBrain(this);
		super.tick();
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}
}
