package addsynth.energy.lib.energy_network;

import addsynth.core.util.math.number.DecimalNumber;
import addsynth.energy.lib.main.IEnergyConsumer;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Represents a list of machines that can receive energy. */
public class ReceiverData<R extends BlockEntity & IEnergyConsumer> extends EnergyTransferData<R> implements IReceiverData {
// this has to be ReceiverData<R extends BlockEntity & IEnergyConsumer> right now instead of
// specifing 'TileAbstractMachine' as the type parameter because Tiles that have their own
// BlockNetwork CANNOT extend from TileAbstractMachine, so they have to be their own AbstractTile class.
// I wonder if the solution is to somehow make a hybrid network class, like a new type of
// Block Network that extends from the Energy Network. anyway, once I solve this issue,
// then it can return to 'extends EnergyTransferData<TileAbstractMachine>'

  private long total_energy;
  private long[] energy = new long[0];
  
  @Override
  public final void update(){
    list.removeIf((EnergyNode<R> node) -> node.isInvalid());
    total_energy = 0;
    size = list.size();
    if(energy.length != size){
      energy = new long[size];
    }
    for(int i = 0; i < size; i++){
      energy[i] = (long)(list.get(i).getTile().getRequestedEnergy() * DecimalNumber.DECIMAL_ACCURACY);
      total_energy += energy[i];
    }
  }
  
  @Override
  public boolean hasRequestedEnergy(){
    return total_energy > 0;
  }
  
  @Override
  public long getTotalRequestedEnergy(){
    return total_energy;
  }
  
  @Override
  public long[] getReceiverValues(){
    return energy;
  }
  
  @Override
  public void receiveEnergy(final int index, final long energy){
    total_energy -= energy;
    this.energy[index] -= energy;
    list.get(index).getEnergy().receiveEnergy((double)energy / DecimalNumber.DECIMAL_ACCURACY);
  }

}
