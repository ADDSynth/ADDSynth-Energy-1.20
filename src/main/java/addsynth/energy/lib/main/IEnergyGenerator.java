package addsynth.energy.lib.main;

/** This makes the Energy Network treat this TileEntity as a Generator. */
public interface IEnergyGenerator extends IEnergyUser {

  /** Returns the energy this TileEntity can produce. */
  public double getAvailableEnergy();

  /** The Energy Network will prioritize using energy from free sources, such as Solar Panels. */
  public boolean isFreeEnergy();

}
