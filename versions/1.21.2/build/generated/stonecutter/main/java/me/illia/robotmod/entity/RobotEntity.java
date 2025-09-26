package me.illia.robotmod.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.illia.robotmod.Robotmod;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.screen.RobotScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
//? if = 1.21.8 {
/*import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
*///?}
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

	//? if = 1.21.8 {
	/*@Override
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
	*///?} else {

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		actions.clear();
		if (nbt.contains("actions", NbtElement.LIST_TYPE)) {
			NbtList list = nbt.getList("actions", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < list.size(); i++) {
				NbtCompound actionNbt = list.getCompound(i);
				Action action = Action.CODEC.decoder().decode(NbtOps.INSTANCE, actionNbt)
					.getOrThrow().getFirst();
				actions.add(action);
			}
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		NbtList list = new NbtList();
		for (Action action : actions) {
			NbtElement actionNbt = Action.CODEC.encoder().encodeStart(NbtOps.INSTANCE, action)
				.getOrThrow();
			list.add(actionNbt);
		}
		nbt.put("actions", list);
		return nbt;
	}
	//?}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!this.getWorld().isClient && !player.isSneaking()) {
			player.openHandledScreen(new ExtendedScreenHandlerFactory<ArrayList<Action>>() {
				@Override
				public ArrayList<Action> getScreenOpeningData(ServerPlayerEntity player) {
					return actions;
				}

				private int id = RobotEntity.this.getId();
				private final PropertyDelegate delegate = new PropertyDelegate() {
					@Override
					public int get(int index) {
						return id;
					}

					@Override
					public void set(int index, int value) {
						id = value;
					}

					@Override
					public int size() {
						return 1;
					}
				};

				@Override
				public Text getDisplayName() {
					return Text.translatable("menu.robotmod.robot");
				}

				@Override
				public RobotScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new RobotScreenHandler(syncId, actions);
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
			new MoveToWalkTarget<>(),
			new ExecuteTask<>()
		);
	}

	@Override
	public BrainActivityGroup<RobotEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(
			new SetPlayerLookTarget<>()
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
