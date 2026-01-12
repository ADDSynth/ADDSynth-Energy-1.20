package addsynth.energy.lib.tiles.machines.switchable;

import java.util.function.Predicate;
import addsynth.core.game.inventory.*;
import addsynth.core.game.inventory.machine.IMachineInventory;
import addsynth.core.game.inventory.machine.MachineInventory;
import addsynth.energy.lib.config.MachineData;
import addsynth.energy.lib.tiles.machines.MachineState;
import addsynth.energy.lib.tiles.machines.TileStandardWorkMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This is just like the {@link TileStandardWorkMachine} except it can be turned off and has idle energy.
 * @author ADDSynth
 */
// UNUSED: addsynth.energy.lib.tiles.machines.switchable.TileStandardWorkMachineWithPower
public abstract class TileStandardWorkMachineWithPower extends TileSwitchableMachine implements IMachineInventory {

  protected final MachineInventory inventory;

  public TileStandardWorkMachineWithPower(BlockEntityType type, BlockPos position, BlockState blockstate,
                                 SlotData[] slots, int output_slots, MachineData data){
    super(type, position, blockstate, MachineState.IDLE, data);
    this.inventory = new MachineInventory(slots, output_slots);
  }

  public TileStandardWorkMachineWithPower(BlockEntityType type, BlockPos position, BlockState blockstate,
                                 int input_slots, Predicate<ItemStack> filter, int output_slots, MachineData data){
    super(type, position, blockstate, MachineState.IDLE, data);
    this.inventory = new MachineInventory(input_slots, filter, output_slots);
  }

  @Override
  protected final void machine_tick(){
    // StandardWorkMachineWithPower has an IDLE state.
    if(inventory.tick()){
      changed = true;
    }
    switch(state){
    case OFF:
      if(power_switch){
        if(power_on_time > 0){
          state = MachineState.POWERING_ON;
        }
        else{
          state = MachineState.IDLE;
        }
        changed = true;
      }
      break;

    case POWERING_ON:
      power_time += 1;
      if(power_time >= power_on_time){
        state = MachineState.IDLE;
        power_time = 0;
      }
      changed = true;
      break;

    case POWERING_OFF:
      powering_off();
      break;

    case IDLE:
      if(power_switch == false){
        turn_off();
      }
      else{
        if(can_work()){
          state = MachineState.RUNNING;
          begin_work();
          changed = true;
        }
      }
      break;
      
    case RUNNING:
      if(canFinishWork()){
        finishWork();
        if(power_switch == false){
          turn_off();
        }
        else{
          if(can_work()){
            begin_work();
          }
          else{
            state = MachineState.IDLE;
          }
        }
        changed = true;
      }
      else{
        if(power_switch == false){
          turn_off();
        }
      }
      machine_running();

      break;
    }
  }

  /** Override this to specify additional instructions while the machine is actively working on something.
   *  This is called every tick on the server side. There is no need to call the super method! */
  protected void machine_running(){}

  /** Called multiple times a tick. Returns whether the machine can perform work.
   *  Override to specify non-default behaviour.
   */
  protected boolean can_work(){
    return inventory.can_work();
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
    if(state == MachineState.OFF){
      return 0;
    }
    return data.get_idle_energy();
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
  
}
