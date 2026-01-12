package addsynth.energy.lib.tiles.machines;

import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.main.Energy;
import addsynth.energy.lib.main.IEnergyConsumer;
import addsynth.energy.lib.main.Receiver;
import addsynth.energy.lib.tiles.AbstractEnergyTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** All machines that only receive energy to do work derive from this class. */
public abstract class TileAbstractMachine extends AbstractEnergyTile implements IEnergyConsumer {

  protected final Receiver energy;
  /** Do not call {@link #update_data()}. Instead set this to true whenever data is changed. */
  protected boolean changed;

  public TileAbstractMachine(final BlockEntityType type, BlockPos position, BlockState blockstate, final Receiver energy){
    super(type, position, blockstate);
    this.energy = energy;
  }

  @Override
  public final void serverTick(ServerLevel level, BlockState blockstate){
    EnergyNetwork.handler.tick(network, level, this);
    derivedTick(level, blockstate);
    if(energy.tick()){
      changed = true;
    }
    if(changed){
      update_data();
      changed = false;
    }
  }

  /** Now that all machines including Generators and Batteries are a part of the {@link EnergyNetwork},
   *  sort of, they all need to be responsible for initializing and ticking the Block Network.
   *  For this reason, we have pre-defined the {@link #serverTick()} method to automatically
   *  work on the EnergyNetwork, tick the {@link Energy}, and check the changed variable, so you
   *  don't need to do any of this when your TileEntities override this tick function.
   *  @sinec ADDSynthEnergy 1.0
   */
  protected void derivedTick(ServerLevel level, BlockState blockstate){
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    energy.loadFromNBT(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    energy.saveToNBT(nbt);
  }
  
  @Override
  public double getRequestedEnergy(){
    return energy.getRequestedEnergy();
  }

  @Override
  public Receiver getEnergy(){
    return energy;
  }

  // This function is only used in guis
  public int getTimeLeft(){
    final double rate = energy.getDifference();
    return rate > 0 ? (int)Math.ceil(energy.getEnergyNeeded() / rate) : 0;
  }

}
