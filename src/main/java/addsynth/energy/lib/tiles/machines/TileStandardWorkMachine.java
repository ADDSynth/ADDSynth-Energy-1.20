package addsynth.energy.lib.tiles.machines;

import java.util.function.Predicate;
import addsynth.core.game.inventory.*;
import addsynth.core.game.inventory.machine.IMachineInventory;
import addsynth.core.game.inventory.machine.MachineInventory;
import addsynth.core.util.network.NetworkUtil;
import addsynth.energy.gameplay.NetworkHandler;
import addsynth.energy.lib.config.MachineData;
import addsynth.energy.lib.main.Receiver;
import addsynth.energy.lib.network_messages.UpdateClientMachineStatusMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The Standard Work Machine, has an idle state, and when it can do work it transfers an
 *  item from the input inventory to the working inventory and switches to the Running state.
 * @author ADDSynth
 */
public abstract class TileStandardWorkMachine extends TileAbstractWorkMachine implements IMachineInventory {

  protected final MachineInventory inventory;

  public TileStandardWorkMachine(BlockEntityType type, BlockPos position, BlockState blockstate,
                                 SlotData[] slots, int output_slots, MachineData data){
    super(type, position, blockstate, MachineState.IDLE, data);
    this.inventory = new MachineInventory(slots, output_slots);
  }

  public TileStandardWorkMachine(BlockEntityType type, BlockPos position, BlockState blockstate,
                                 int input_slots, Predicate<ItemStack> filter, int output_slots, MachineData data){
    super(type, position, blockstate, MachineState.IDLE, data);
    this.inventory = new MachineInventory(input_slots, filter, output_slots);
  }

  /** Use this constructor if your machine wants to set it's energy values and work time dynamically
   *  during gameplay. However, if the data remains static, then you should actually define your own
   *  {@link MachineData} object as a static final field, and use one of the other constructors.
   */
  public TileStandardWorkMachine(BlockEntityType type, BlockPos position, BlockState blockstate,
                                 int input_slots, Predicate<ItemStack> filter, int output_slots){
    super(type, position, blockstate, MachineState.IDLE, new Receiver());
    this.inventory = new MachineInventory(input_slots, filter, output_slots);
  }

  @Override
  protected void derivedTick(ServerLevel level, BlockState blockstate){
    machine_tick();
    if(inventory.tick()){
      changed = true;
    }
    // I used to check machine status and only update the client if it had changed, but this doesn't update my client
    // on the first tick because I hadn't logged in yet. And it never triggered on any tick after that.
    NetworkUtil.send_to_TileEntity(NetworkHandler.INSTANCE, this, new UpdateClientMachineStatusMessage(this.worldPosition, status));
  }

  @Override
  protected final void machine_tick(){
    switch(state){
    case RUNNING:
      status = energy.isReceiving() ? MachineStatus.GOOD : MachineStatus.NOT_RECEIVING_ENERGY;
      if(canFinishWork()){
        finishWork();
        if(can_work()){
          begin_work();
        }
        else{
          state = MachineState.IDLE;
        }
        changed = true;
      }
      machine_running();
      break;

    case IDLE:
      if(can_work()){
        state = MachineState.RUNNING;
        begin_work();
        changed = true;
      }
      break;
      
    default:
      state = MachineState.IDLE;
      changed = true;
    }
  }

  /** Override this to specify additional instructions while the machine is actively working on something.
   *  This is called every tick on the server side. There is no need to call the super method! */
  protected void machine_running(){}

  /** Called multiple times a tick. Returns whether the machine can perform work.
   *  Override to specify non-default behaviour.
   */
  protected boolean can_work(){
    if(inventory.can_add_to_output()){
      status = MachineStatus.GOOD;
      return inventory.can_work();
    }
    status = MachineStatus.OUTPUT_FULL;
    return false;
  }

  /** This is called to start a job.
   *  Override to specify non-default behaviour.
   */
  protected void begin_work(){
    inventory.begin_work();
  }

  protected boolean canFinishWork(){
    return energy.isFull();
  }

  /** Finishes working on the center ItemStack and increments the output.
   *  Override to specify non-default behaviour.
   */
  protected void finishWork(){
    inventory.finish_work();
    energy.setEmpty();
  }

  @Override
  public void onInventoryChanged(){
    changed = true;
  }

  @Override
  public int getJobs(){
    return inventory.getJobs();
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    inventory.loadFromNBT(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    inventory.saveToNBT(nbt);
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side){
    if(remove == false){
      if(capability == ForgeCapabilities.ITEM_HANDLER){
        return InventoryUtil.getInventoryCapability(inventory.getInputInventory(), inventory.getOutputInventory(), side);
      }
      return super.getCapability(capability, side);
    }
    return LazyOptional.empty();
  }

  @Override
  public double getRequestedEnergy(){
    if(state == MachineState.RUNNING){
      return energy.getRequestedEnergy();
    }
    return 0;
  }

  @Override
  public final void drop_inventory(){
    inventory.drop(worldPosition, level);
  }

  @Override
  public final InputInventory getInputInventory(){
    return inventory.getInputInventory();
  }
  
  @Override
  public final OutputInventory getOutputInventory(){
    return inventory.getOutputInventory();
  }

  @Override
  public final CommonInventory getWorkingInventory(){
    return inventory.getWorkingInventory();
  }

  @Override
  public int getTimeLeft(){
    final double rate = energy.getDifference();
    if(rate > 0){
      return (int)Math.ceil((energy.getEnergyNeeded() + inventory.getJobs() * energy.getCapacity()) / rate);
    }
    return 0;
  }
  
}
