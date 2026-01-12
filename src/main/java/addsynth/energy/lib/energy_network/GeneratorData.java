package addsynth.energy.lib.energy_network;

import addsynth.core.util.math.number.DecimalNumber;
import addsynth.energy.lib.main.IEnergyGenerator;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Represents a list of machines that can have energy extracted from them. */
public class GeneratorData<G extends BlockEntity & IEnergyGenerator> extends EnergyTransferData<G> implements IGeneratorData {
// Now I'm adding the TileUniversalEnergyInterface and it's the same problem.
// I can't specify TileAbstractGenerator as the type parameter.

  private long total_energy;
  private long[] energy = new long[0];

  @Override
  public final void update(){
    list.removeIf((EnergyNode<G> node) -> node.isInvalid());
    total_energy = 0;
    size = list.size();
    if(energy.length != size){
      energy = new long[size];
    }
    for(int i = 0; i < size; i++){
      energy[i] = (long)(list.get(i).getTile().getAvailableEnergy() * DecimalNumber.DECIMAL_ACCURACY);
      total_energy += energy[i];
    }
  }

  @Override
  public boolean hasAvailableEnergy(){
    return total_energy > 0;
  }
  
  @Override
  public long getTotalAvailableEnergy(){
    return total_energy;
  }
  
  @Override
  public long[] getGeneratorValues(){
    return energy;
  }
  
  @Override
  public void extractEnergy(final int index, final long energy){
    total_energy -= energy;
    this.energy[index] -= energy;
    list.get(index).getEnergy().extractEnergy((double)energy / DecimalNumber.DECIMAL_ACCURACY);
  }

}
