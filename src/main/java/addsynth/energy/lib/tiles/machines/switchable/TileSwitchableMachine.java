package addsynth.energy.lib.tiles.machines.switchable;

import addsynth.core.util.java.StringUtil;
import addsynth.core.util.math.common.RoundMode;
import addsynth.core.util.network.NetworkUtil;
import addsynth.energy.gameplay.NetworkHandler;
import addsynth.energy.lib.config.MachineData;
import addsynth.energy.lib.network_messages.SwitchMachineMessage;
import addsynth.energy.lib.network_messages.UpdateClientMachineStatusMessage;
import addsynth.energy.lib.tiles.machines.MachineState;
import addsynth.energy.lib.tiles.machines.TileAbstractWorkMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Machines with a Power Switch can be switched off to conserve energy.
 *  @author ADDSynth
 */
public abstract class TileSwitchableMachine extends TileAbstractWorkMachine implements ISwitchableMachine {
// TODO: I was against it all this time, but yeah, SWITCH TO A BEHAVIOUR SYSTEM!!!!
//       That way, certain machines can derive from standardized abstract classes, but also customize their behaviour.
//       No longer use an enum to define state. Have State just defined as int constants in behaviour, only define the states that behaviour uses.

  protected boolean power_switch;
  protected int power_time;
  protected int power_on_time;
  protected int power_off_time;

  public TileSwitchableMachine(BlockEntityType type, BlockPos position, BlockState blockstate,
                               MachineState initial_state, MachineData data){
    this(type, position, blockstate, initial_state, data, true);
  }

  public TileSwitchableMachine(BlockEntityType type, BlockPos position, BlockState blockstate,
                               MachineState initial_state, MachineData data, boolean initial_power_state){
    super(type, position, blockstate, initial_state, data);
    power_on_time  = data.get_power_time();
    power_off_time = data.get_power_time();
    power_switch = initial_power_state;
  }

  @Override
  protected void derivedTick(final ServerLevel level, final BlockState blockstate){
    // Check if Power data changed
    final int power_time = data.get_power_time();
    if(power_time != power_on_time){
      power_on_time  = power_time;
      power_off_time = power_time;
      changed = true;
    }
    machine_tick();
    NetworkUtil.send_to_TileEntity(NetworkHandler.INSTANCE, this, new UpdateClientMachineStatusMessage(this.worldPosition, status));
  }

  @Override
  protected void machine_tick(){
    switch(state){
    case OFF:
      if(power_switch){
        if(power_on_time > 0){
          state = MachineState.POWERING_ON;
        }
        else{
          state = MachineState.RUNNING;
        }
        changed = true;
      }
      break;

    case POWERING_ON:
      power_time += 1;
      if(power_time >= power_on_time){
        state = MachineState.RUNNING;
        power_time = 0;
      }
      changed = true;
      break;

    case POWERING_OFF:
      powering_off();
      break;

    default:
      running();
    }
  }

  protected abstract void running();

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    power_switch = nbt.getBoolean("Power Switch");
    power_time   = nbt.getInt("Power Time");
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    nbt.putBoolean("Power Switch", power_switch);
    nbt.putInt("Power Time", power_time);
  }

  /** This should only be called by the TileEntity's {@link #machine_tick()}
   *  method while in the {@link MachineState#POWERING_OFF POWERING_OFF} state. */
  protected final void powering_off(){
    power_time += 1;
    if(power_time >= power_off_time){
      state = MachineState.OFF;
      power_time = 0;
    }
    changed = true;
  }

  /** This is called after switching off the power switch, either by a
   *  {@link SwitchMachineMessage} or by a TileEntity that implements
   *  the {@link IAutoShutoff} interface and checks if the auto shutoff
   *  is enabled, and shuts itself off after performing work. This will
   *  put the machine in the {@link MachineState#POWERING_OFF POWERING_OFF}
   *  state if the machine has {@code power_cycle_time} > 0.
   */
  protected final void turn_off(){
    if(power_off_time > 0){
      state = MachineState.POWERING_OFF;
    }
    else{
      state = MachineState.OFF;
    }
    changed = true;
  }

  public final float getPowerCycleTimePercentage(){
    if(state == MachineState.POWERING_ON){
      if(power_on_time > 0){
        return (float)power_time / power_on_time;
      }
    }
    if(state == MachineState.POWERING_OFF){
      if(power_off_time > 0){
        return (float)power_time / power_off_time;
      }
    }
    return 0.0f;
  }

  @Override
  public void togglePowerSwitch(){
    if(onServerSide()){
      power_switch = !power_switch;
      changed = true;
    }
  }

  @Override
  public final boolean get_switch_state(){
    return power_switch;
  }

  @Override
  public MutableComponent getStatus(){
    if(status.isError()){
      return status.get();
    }
    if(state == MachineState.POWERING_OFF || state == MachineState.POWERING_ON){
      return state.get(StringUtil.toPercentageString(getPowerCycleTimePercentage(), RoundMode.Floor));
    }
    return state.get();
  }

}
