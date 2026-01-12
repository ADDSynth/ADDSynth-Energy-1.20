package addsynth.energy.lib.energy_network;

public interface IReceiverData {

  public boolean hasRequestedEnergy();
  public long getTotalRequestedEnergy();
  public long[] getReceiverValues();
  public void receiveEnergy(int index, long energy);

}
