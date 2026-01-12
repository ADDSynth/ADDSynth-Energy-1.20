package addsynth.energy.lib.main;

/** Use this on TileEntities that can both receive and extract energy. */
public interface IBattery extends IEnergyConsumer, IEnergyGenerator {

  @Override
  public default boolean isFreeEnergy(){
    return false;
  }

}
