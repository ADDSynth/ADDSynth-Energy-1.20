package addsynth.energy.lib.items.energy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Items are a special case. Capabilities are attached to ItemStacks in the
 *  {@link Item#initCapabilities(ItemStack, CompoundTag) initCapabilities()}
 *  method, which MUST return an {@link ICapabilityProvider}. They only exist
 *  for the life of the ItemStack.
 */
public final class EnergyItemCapabilityProvider implements ICapabilityProvider {

  private final LazyOptional<IEnergyStorage> capability;

  private EnergyItemCapabilityProvider(IEnergyStorage energy_storage){
    capability = LazyOptional.of(() -> energy_storage);
  }

  public static final EnergyItemCapabilityProvider create(ItemStack stack, int max_energy){
    return new EnergyItemCapabilityProvider(new ItemEnergy(stack, max_energy));
  }

  public static final EnergyItemCapabilityProvider createChargeOnly(ItemStack stack, int max_energy){
    return new EnergyItemCapabilityProvider(new ChargeOnlyItemEnergy(stack, max_energy));
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side){
    if(cap == ForgeCapabilities.ENERGY){
      return capability.cast();
    }
    return LazyOptional.empty();
  }

}
