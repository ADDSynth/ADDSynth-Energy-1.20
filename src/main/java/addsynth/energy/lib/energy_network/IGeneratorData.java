package addsynth.energy.lib.energy_network;

public interface IGeneratorData {

  public boolean hasAvailableEnergy();
  public long getTotalAvailableEnergy();
  public long[] getGeneratorValues();
  public void extractEnergy(int index, long energy);

}
