package addsynth.energy.lib.main;

import net.minecraft.nbt.CompoundTag;

/** The Passive Generator is a Generator which 'produces' energy every tick
 *  based on the TileEntity's internal state, and thus can be calculated from
 *  the TileEntity and does not need to save any energy values.
 */
public class PassiveGenerator extends Generator {

  /** You must call this at least once to set the Capacity and Max Extract variables.<br>
   *  If available energy is expected to change every tick, then you should call {@link #setAll} instead. */
  public void set(final double max_energy){
    capacity.set(max_energy);
    maxExtract.set(max_energy);
  }

  /** If Capacity is not expected to change very often, but is controlled by some other means,<br/>
   *  then you can call this as a simple helper that sets Capacity and MaxExtract for you if it changes. */
  public void setIfChanged(final double max_energy){
    if(capacity.get() != max_energy){
      capacity.set(max_energy);
      maxExtract.set(max_energy);
    }
  }

  /** Call this AFTER you have called {@link #set} at least once,
   *  or you also call {@link #setIfChanged} in your tick function. */
  public void reset(){
    energy.set(capacity.get());
    energy_out.set(0);
  }

  /** Call this if your energy is expected to change every tick. */
  public void setAll(final double energy_provided){
    energy.set(energy_provided);
    capacity.set(energy_provided);
    maxExtract.set(energy_provided);
    energy_out.set(0);
  }

  @Override
  @Deprecated
  public void loadFromNBT(final CompoundTag nbt){
  }

  @Override
  @Deprecated
  public void saveToNBT(final CompoundTag nbt){
  }

  @Override
  @Deprecated
  public boolean tick(){
    return false;
  }

  @Override
  @Deprecated
  public void updateEnergyIO(){
  }

}
