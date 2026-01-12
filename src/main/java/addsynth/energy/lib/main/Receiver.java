package addsynth.energy.lib.main;

import net.minecraft.nbt.CompoundTag;

/** A Receiver is an Energy object that can never have energy extracted
 *  from it. Since we know this, we can override certain methods to make
 *  them more efficient.
 */
public class Receiver extends Energy {

  public Receiver(){
    super(0, 0, 0, 0);
  }
  
  public Receiver(final double capacity){
    super(capacity, capacity, 0, 0);
  }

  public Receiver(final double capacity, final double maxReceive){
    super(capacity, maxReceive, 0, 0);
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
    this.maxReceive.set(     energy_tag.getDouble("MaxReceive"));
    this.energy_in.set(      energy_tag.getDouble("Energy In"));
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
    energy_tag.putDouble("MaxReceive", this.maxReceive.get());
    energy_tag.putDouble("Energy In",  this.energy_in.get());
    nbt.put("EnergyStorage", energy_tag);
  }

// ================================= SETTERS =====================================

  @Override
  public final void setMaxExtract(final double maxExtract){
  }

  @Override
  public final void setTransferRate(final int transferRate){
    this.maxReceive.set(transferRate);
    changed = true;
  }

  @Override
  public void set(final Energy energy){
    this.energy.set(     energy.getEnergy()     );
    this.capacity.set(   energy.getCapacity()   );
    this.maxReceive.set( energy.getMaxReceive() );
    this.energy_in.set(  energy.get_energy_in() );
    changed = true;
  }

// ================================== GETTERS =================================

  @Override
  public final double getAvailableEnergy(){
    return 0;
  }

  @Override
  public final double getMaxExtract(){
    return 0;
  }

  @Override
  public final double get_energy_out(){
    return 0;
  }

  @Override
  public final double getDifference(){
    return energy_in.get();
  }

// =================================== QUERIES ======================================

  @Override
  public final boolean canExtract(){
    return false;
  }

  public final boolean isReceiving(){
    return energy_in.get() > 0;
  }

}
