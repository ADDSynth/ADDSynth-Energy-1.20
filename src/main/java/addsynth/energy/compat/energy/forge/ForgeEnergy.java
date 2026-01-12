package addsynth.energy.compat.energy.forge;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public final class ForgeEnergy {

  public static final boolean check(final BlockEntity tile, final Direction capability_side){
    return tile.getCapability(ForgeCapabilities.ENERGY, capability_side).orElse(null) != null;
  }

  /** Gets energy from this ForgeEnergy object. Use 'simulate' parameter to simulate or actually extract energy. */
  public static final int get(final Object input, final int energy_requested, final boolean simulate){
    final IEnergyStorage energy = (IEnergyStorage)input;
    if(energy.canExtract()){
      return energy.extractEnergy(energy_requested, simulate);
    }
    return 0;
  }

  public static final int send(final Object input, final int transmitted_energy){
    final IEnergyStorage energy = (IEnergyStorage)input;
    if(energy.canReceive()){
      return energy.receiveEnergy(transmitted_energy, false);
    }
    return 0;
  }
  
}
