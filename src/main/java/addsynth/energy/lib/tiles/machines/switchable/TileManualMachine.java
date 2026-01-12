package addsynth.energy.lib.tiles.machines.switchable;

import addsynth.energy.lib.config.MachineData;
import addsynth.energy.lib.tiles.machines.MachineState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Manual Machines can be switched off, but only accept energy when they're on.
 *  They do not perform any action automatically and you must check for and empty
 *  energy yourself.
 * @author ADDSynth
 */
public abstract class TileManualMachine extends TileSwitchableMachine {

  public TileManualMachine(final BlockEntityType type, BlockPos position, BlockState blockstate, final MachineData data){
    super(type, position, blockstate, MachineState.RUNNING, data);
  }

  public TileManualMachine(final BlockEntityType type, BlockPos position, BlockState blockstate,
                           final MachineData data, final boolean initial_power_state){
    super(type, position, blockstate, initial_power_state ? MachineState.RUNNING : MachineState.OFF, data, initial_power_state);
  }

  @Override
  protected final void running(){
    if(power_switch == false){
      turn_off();
    }
  }

  @Override
  public double getRequestedEnergy(){
    if(state == MachineState.RUNNING){
      return energy.getRequestedEnergy();
    }
    return 0;
  }

}
