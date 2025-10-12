package me.illia.robotmod.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.illia.robotmod.Util;
import me.illia.robotmod.actions.Action;
import me.illia.robotmod.actions.ActionRunner;
import me.illia.robotmod.block.LunarPanelBlock;
import me.illia.robotmod.block.ModBlocks;
import me.illia.robotmod.networking.RobotActionsSyncC2SPayload;
import me.illia.robotmod.screen.RobotScreenHandler;
import me.illia.robotmod.screen.RobotScreenHandlerData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
/*import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
*///?} else {
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.inventory.Inventories;
import me.illia.robotmod.Robotmod;
//?}
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
	public int actionI = -1;

	public RobotEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		this.actions = new ArrayList<>();
		this.inv = new SimpleInventory(16);
		this.home = getBlockPos();
	}

	//? if >= 1.21.6 {
	/*@Override
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
	*///?} else {

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains("actions")) {
			this.actions = new ArrayList<>(
				Action.CODEC.codec().listOf()
					.parse(NbtOps.INSTANCE, nbt.get("actions"))
					.resultOrPartial(error -> Robotmod.LOGGER.error("Failed to read actions: {}", error))
					.orElse(List.of())
			);
		} else {
			this.actions = new ArrayList<>();
		}

		inv = new SimpleInventory(16);
		Inventories.readNbt(nbt, inv.heldStacks, getWorld().getRegistryManager());
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		Action.CODEC.codec().listOf()
			.encodeStart(NbtOps.INSTANCE, this.actions)
			.resultOrPartial(error -> Robotmod.LOGGER.error("Failed to write actions: {}", error))
			.ifPresent(nbtElement -> nbt.put("actions", nbtElement));

		Inventories.writeNbt(nbt, inv.heldStacks, getWorld().getRegistryManager());

		return nbt;
	}
	//?}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!this.getWorld().isClient && !player.isSneaking() && !Util.night(this.getWorld())) {
			player.openHandledScreen(new ExtendedScreenHandlerFactory<RobotScreenHandlerData>() {
				private final int id = RobotEntity.this.getId();
				private final RobotScreenHandlerData robotScreenHandlerData = new RobotScreenHandlerData(id, RobotEntity.this.inv.heldStacks);

				@Override
				public RobotScreenHandlerData getScreenOpeningData(ServerPlayerEntity player) {
					return robotScreenHandlerData;
				}

				@Override
				public Text getDisplayName() {
					return Util.t("menu.robotmod.robot");
				}

				@Override
				public RobotScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
					return new RobotScreenHandler(syncId, playerInventory, robotScreenHandlerData);
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

		if (Util.nearest(this, 35, state -> state.isOf(ModBlocks.LUNAR_PANEL_BLOCK) && state.get(LunarPanelBlock.ACTIVE))) {
			if (!ranActions) {
				int i = 0;
				for (Action action : actions) {
					ActionRunner.run(action, this, i);
					actionI = i;

					List<ServerPlayerEntity> serverPlayers = world.getPlayers();
					for (ServerPlayerEntity serverPlayer : serverPlayers) {
						ServerPlayNetworking.send(serverPlayer, new UpdateActionDebugS2CPayload(actionI, getId()));
					}

					i++;
				}
				ranActions = true;
			}
		} else {
			ranActions = false;
			actionI = -1;

			List<ServerPlayerEntity> serverPlayers = world.getPlayers();
			for (ServerPlayerEntity serverPlayer : serverPlayers) {
				ServerPlayNetworking.send(serverPlayer, new UpdateActionDebugS2CPayload(actionI, getId()));
			}
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

	@Override
	protected void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer) {
		int invI = 0;
		for (ItemStack stack : inv.heldStacks) {
			if (invI != slot) {
				dropStack(world, stack);
			}

			invI++;
		}
		super.dropLoot(world, damageSource, causedByPlayer);
	}
}
