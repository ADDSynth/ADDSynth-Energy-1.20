package addsynth.energy.lib.energy_network;

import addsynth.core.util.java.list.IndexedSet;
import addsynth.core.util.math.number.DecimalNumber;
import addsynth.energy.gameplay.machines.universal_energy_interface.TileUniversalEnergyInterface;

/** Machines which cannot be standardized as a Generator, Receiver, or Battery, or otherwise
 *  just want total control over how the Energy System interacts with their Energy, should
 *  determine for themselves how energy should be received/extracted at every transfer stage.
 *  The {@link EnergyTransferStage} will then be passed to the TileEntity so they can determine
 *  whether to provide energy values during that stage. Although, right now, the only machine
 *  which acts this way is the {@link TileUniversalEnergyInterface}. Unlike normal
 *  {@link EnergyTransferData} energy values for custom data MUST be reacquired in each transfer stage.
 */
public class CustomTransferData {

  protected final IndexedSet<EnergyNode<TileUniversalEnergyInterface>> list = new IndexedSet<>();
  private int i;
  private int size;
  private long total_generator_energy;
  private long total_receiver_energy;
  private long[] generator_energy = new long[0];
  private long[]  receiver_energy = new long[0];

  public final void clear(){
    list.clear();
  }

  public final void add(final TileUniversalEnergyInterface tile){
    list.add(new EnergyNode<>(tile));
  }

  public final void update(){
    list.removeIf((EnergyNode<TileUniversalEnergyInterface> node) -> node.isInvalid());
    size = list.size();
    if(generator_energy.length != size){
      generator_energy = new long[size];
       receiver_energy = new long[size];
    }
  }

  public final long getAvailableEnergy(final EnergyTransferStage stage){
    total_generator_energy = 0;
    for(i = 0; i < size; i++){
          generator_energy[i] = (long)(list.get(i).getTile().getAvailableEnergy(stage) * DecimalNumber.DECIMAL_ACCURACY);
      total_generator_energy += generator_energy[i];
    }
    return total_generator_energy;
  }

  public final long getRequestedEnergy(final EnergyTransferStage stage){
    total_receiver_energy = 0;
    for(i = 0; i < size; i++){
          receiver_energy[i] = (long)(list.get(i).getTile().getRequestedEnergy(stage) * DecimalNumber.DECIMAL_ACCURACY);
      total_receiver_energy += receiver_energy[i];
    }
    return total_receiver_energy;
  }

  public final long[] getGeneratorValues(){
    return generator_energy;
  }

  public final long[] getReceiverValues(){
    return receiver_energy;
  }

  public final void extractEnergy(final int index, final long energy){
    list.get(index).getEnergy().extractEnergy((double)energy / DecimalNumber.DECIMAL_ACCURACY);
  }

  public final void receiveEnergy(final int index, final long energy){
    list.get(index).getEnergy().receiveEnergy((double)energy / DecimalNumber.DECIMAL_ACCURACY);
  }

}
