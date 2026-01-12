package addsynth.energy.lib.tiles.machines;

import addsynth.energy.lib.config.MachineData;
import addsynth.energy.lib.main.MachineReceiver;
import addsynth.energy.lib.main.Receiver;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Work Machines are the most commonly used Machine type. Their behaviour is
 *  specifically defined and managed.
 * @author ADDSynth
 * @since Overpowered Technology version 1.3.4, October 29, 2020 (WorkSystem commit)
 */
public abstract class TileAbstractWorkMachine extends TileAbstractMachine {

  protected final MachineData data;
  protected MachineState state;
  protected MachineStatus status = MachineStatus.GOOD;

  public TileAbstractWorkMachine(BlockEntityType type, BlockPos position, BlockState blockstate, MachineState initial_state, MachineData data){
    super(type, position, blockstate, new MachineReceiver(data));
    this.data = data;
    this.state = initial_state;
  }

  public TileAbstractWorkMachine(final BlockEntityType type, BlockPos position, BlockState blockstate,
                                 final MachineState initial_state, final Receiver energy){
    super(type, position, blockstate, energy);
    this.data = null;
    this.state = initial_state;
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    state = MachineState.value[nbt.getInt("State")];
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    nbt.putInt("State", state.ordinal());
  }

  protected abstract void machine_tick();
  
  /** Set MachineStatus on Clients from the Server. */
  public final void setStatus(final MachineStatus status){
    this.status = status;
  }

  public final float getWorkTimePercentage(){
    return energy.getEnergyPercentage();
  }

  public MutableComponent getStatus(){
    return status.isError() ? status.get() : state.get();
  }

}
