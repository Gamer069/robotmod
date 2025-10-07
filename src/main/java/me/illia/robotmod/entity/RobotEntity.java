package me.illia.robotmod.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionRunner;
import me.illia.robotmod.networking.RobotActionsSyncC2SPayload;
import me.illia.robotmod.screen.RobotScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
//? if >= 1.21.6 {
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
//?} else {
/*import net.minecraft.nbt.NbtCompound;
*///?}
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
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
	public BlockPos home;
	public boolean ranActions;
	public SimpleInventory inv;
	public int slot;

	public RobotEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.actions = new ArrayList<>();
		this.inv = new SimpleInventory(16);
		this.home = getBlockPos();
	}

	//? if >= 1.21.6 {
	@Override
	protected void readCustomData(ReadView view) {
		this.actions = new ArrayList<>(view.read("actions", Action.CODEC.codec().listOf()).orElse(List.of()));
		this.home = view.read("home", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
		ReadView.TypedListReadView<ItemStack> invListView = view.getTypedListView("inv", ItemStack.CODEC);
		inv.readDataList(invListView);
		this.slot = view.getInt("slot", 0);

		super.readCustomData(view);
	}

	@Override
	protected void writeCustomData(WriteView view) {
		view.put("actions", Action.CODEC.codec().listOf(), this.actions);
		view.put("home", BlockPos.CODEC, this.home);
		WriteView.ListAppender<ItemStack> invAppender = view.getListAppender("inv", ItemStack.CODEC);
		inv.toDataList(invAppender);

		view.putInt("slot", this.slot);

		super.writeCustomData(view);
	}

	@Override
	public ItemStack getMainHandStack() {
		return inv.getStack(slot);
	}

	@Override
	protected void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer) {
		for (ItemStack stack : inv.heldStacks) {
			dropStack(world, stack);
		}
		super.dropLoot(world, damageSource, causedByPlayer);
	}

	//?} else {

	/*@Override
	public void readNbt(NbtCompound nbt) {
		super.read(nbt);

		if (nbt.contains("actions", NbtElement.LIST_TYPE)) {
			this.actions = new ArrayList<>(
				Action.CODEC.codec().listOf()
					.parse(NbtOps.INSTANCE, nbt.get("actions"))
					.resultOrPartial(error -> LOGGER.error("Failed to read actions: {}", error))
					.orElse(List.of())
			);
		} else {
			this.actions = new ArrayList<>();
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		Action.CODEC.codec().listOf()
			.encodeStart(NbtOps.INSTANCE, this.actions)
			.resultOrPartial(error -> LOGGER.error("Failed to write actions: {}", error))
			.ifPresent(nbtElement -> nbt.put("actions", nbtElement));

		return nbt;
	}
	*///?}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!this.getWorld().isClient && !player.isSneaking() && !Util.night(this.getWorld())) {
			player.openHandledScreen(new ExtendedScreenHandlerFactory<Integer>() {
				private final int id = RobotEntity.this.getId();

				@Override
				public Integer getScreenOpeningData(ServerPlayerEntity player) {
					return id;
				}

				@Override
				public Text getDisplayName() {
					return Text.translatable("menu.robotmod.robot");
				}

				@Override
				public RobotScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new RobotScreenHandler(syncId, id);
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

		if (Util.night(world)) {
			if (!ranActions) {
				for (Action action : actions) {
					ActionRunner.run(action, this);
				}
				ranActions = true;
			}
		} else {
			ranActions = false;
		}

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

	public void save(ArrayList<Action> actions) {
		this.actions = actions;
		ClientPlayNetworking.send(new RobotActionsSyncC2SPayload(getId(), actions.stream().toList(), getWorld().getRegistryKey()));
	}

	@Override
	public boolean canPickUpLoot() {
		return true;
	}

	@Override
	protected void loot(ServerWorld world, ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getStack();
		ItemStack leftover = inv.addStack(stack.copy());
		if (leftover.isEmpty()) {
			itemEntity.discard(); // remove the item if fully picked up
		} else {
			stack.setCount(leftover.getCount()); // leave remaining
		}
		super.loot(world, itemEntity);
	}
}
