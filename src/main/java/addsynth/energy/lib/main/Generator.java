package addsynth.energy.lib.main;

import net.minecraft.nbt.CompoundTag;

/** A Generator is a special Energy object that can never accept energy from
 *  the Energy Network, and thus, we can make certain assumptions and override
 *  some functions to be more efficient. */
public class Generator extends Energy {

  public Generator(){
    super(0, 0, 0, 0);
  }
  
  public Generator(final double capacity){
    super(capacity, 0, capacity, 0);
  }
  
  public Generator(final double capacity, final double maxExtract){
    super(capacity, 0, maxExtract, 0);
  }

// ================================= NBT READ / WRITE =================================

  /**
   * Read and set all values from the data inside the given {@link CompoundTag}
   * @param nbt The {@link CompoundTag} with all the data
   */
  @Override
  public void loadFromNBT(final CompoundTag nbt){
    final CompoundTag energy_tag = nbt.getCompound("EnergyStorage");
    this.energy.set(         energy_tag.getDouble("Energy")    );
    this.capacity.set(       energy_tag.getDouble("Capacity")  );
    this.maxExtract.set(     energy_tag.getDouble("MaxExtract"));
    this.energy_out.set(     energy_tag.getDouble("Energy Out"));
  }

  /**
   * Write all of the data to the {@link CompoundTag} provided
   * @param nbt The {@link CompoundTag} to write to
   */
  @Override
  public void saveToNBT(final CompoundTag nbt){
    final CompoundTag energy_tag = new CompoundTag();
    energy_tag.putDouble("Energy",     this.energy.get());
    energy_tag.putDouble("Capacity",   this.capacity.get());
    energy_tag.putDouble("MaxExtract", this.maxExtract.get());
    energy_tag.putDouble("Energy Out", this.energy_out.get());
    nbt.put("EnergyStorage", energy_tag);
  }

// ================================= SETTERS =====================================

  @Override
  public final void setMaxReceive(final int maxReceive){
  }

  @Override
  public final void setTransferRate(final int transferRate){
    this.maxExtract.set(transferRate);
    changed = true;
  }

  @Override
  public void set(final Energy energy){
    this.energy.set(     energy.getEnergy()     );
    this.capacity.set(   energy.getCapacity()   );
    this.maxExtract.set( energy.getMaxExtract() );
    this.energy_out.set( energy.get_energy_out());
    changed = true;
  }

// ================================== GETTERS =================================

  @Override
  public final double getRequestedEnergy(){
    return 0;
  }

  @Override
  public final double getMaxReceive(){
    return 0;
  }

  @Override
  public final double get_energy_in(){
    return 0;
  }

  @Override
  public final double getDifference(){
    return -energy_out.get();
  }

// =================================== QUERIES ======================================

  @Override
  public final boolean canReceive(){
    return false;
  }

}
