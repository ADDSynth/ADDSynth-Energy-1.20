package addsynth.energy.lib.tiles.machines.switchable;

import addsynth.energy.lib.config.MachineData;
import addsynth.energy.lib.tiles.machines.MachineState;
import addsynth.energy.lib.tiles.machines.MachineStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Passive Machines have no idle state. They are either OFF or RUNNING.
 *  Passive Machines do not have idle energy. */
public abstract class TilePassiveMachine extends TileSwitchableMachine {

  public TilePassiveMachine(final BlockEntityType type, BlockPos position, BlockState blockstate, final MachineData data){
    super(type, position, blockstate, MachineState.RUNNING, data);
  }

  public TilePassiveMachine(final BlockEntityType type, BlockPos position, BlockState blockstate,
                            final MachineData data, final boolean initial_power_state){
    super(type, position, blockstate, initial_power_state ? MachineState.RUNNING : MachineState.OFF, data, initial_power_state);
  }

  @Override
  protected final void running(){
    if(canWork()){
      status = energy.isReceiving() ? MachineStatus.GOOD : MachineStatus.NOT_RECEIVING_ENERGY;
      if(energy.isFull()){
        perform_work();
        energy.setEmpty();
        changed = true;
      }
    }
    if(power_switch == false){
      turn_off();
    }
  }

  protected abstract boolean canWork();

  protected abstract void perform_work();

  @Override
  public double getRequestedEnergy(){
    if(state == MachineState.RUNNING && canWork()){
      return energy.getRequestedEnergy();
    }
    return 0;
  }

}
