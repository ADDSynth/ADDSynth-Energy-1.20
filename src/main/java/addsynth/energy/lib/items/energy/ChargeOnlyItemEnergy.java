package addsynth.energy.lib.items.energy;

import net.minecraft.world.item.ItemStack;

public final class ChargeOnlyItemEnergy extends ItemEnergy {

  public ChargeOnlyItemEnergy(ItemStack stack, int max_energy){
    super(stack, max_energy, 1);
  }

  public ChargeOnlyItemEnergy(ItemStack stack, int max_energy, int maxTransferRate){
    super(stack, max_energy, maxTransferRate);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate){
    return 0;
  }

  @Override
  public boolean canExtract(){
    return false;
  }

}
