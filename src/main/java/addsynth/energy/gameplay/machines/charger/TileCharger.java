package addsynth.energy.gameplay.machines.charger;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import addsynth.energy.lib.main.Receiver;
import addsynth.energy.lib.tiles.machines.MachineStatus;
import addsynth.energy.lib.tiles.machines.TileSingleItemMachine;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public final class TileCharger extends TileSingleItemMachine implements MenuProvider {

  public static final Predicate<ItemStack> filter = (ItemStack stack) -> {
    final LazyOptional<IEnergyStorage> optional = stack.getCapability(ForgeCapabilities.ENERGY);
    return optional.isPresent();
  };

  @Nullable
  private IEnergyStorage item_energy;

  public TileCharger(BlockPos position, BlockState blockstate){
    super(Tiles.CHARGER.get(), position, blockstate, filter, new Receiver(1));
  }

  @Override
  protected final void doWork(){
    if(item_energy != null){
      final IEnergyStorage item_energy = this.item_energy;
      if(energy.isFull() && item_energy.receiveEnergy(1, true) > 0){
        item_energy.receiveEnergy(1, false);
        energy.setEmpty();
      }
    }
  }

  @Override
  protected final boolean canFinishWork(){
    if(output_inventory.isEmpty()){
      status = MachineStatus.GOOD;
      if(item_energy != null){
        final IEnergyStorage item_energy = this.item_energy;
        return item_energy.getEnergyStored() == item_energy.getMaxEnergyStored();
      }
      return true; // if somehow an item was inserted, but does not have an IEnergyStorage
    }
    status = MachineStatus.OUTPUT_FULL;
    return false;
  }

  @Override
  protected final void finishWork(){
    final ItemStack stack = input_inventory.extractItemStack(0);
    output_inventory.add(0, stack);
  }

  @Override
  public final void onInventoryChanged(){
    final ItemStack itemstack = input_inventory.getStackInSlot(0);
    final LazyOptional<IEnergyStorage> optional = itemstack.getCapability(ForgeCapabilities.ENERGY);
    item_energy = optional.orElse(null);
    changed = true;
  }

  /** Since the Charger charges items at a rate of 1 Energy per tick, this returns the
   *  number of ticks needed to charge items to full charge. */
  @Override
  public final int getTimeLeft(){
    if(item_energy != null){
      final IEnergyStorage item_energy = this.item_energy;
      return item_energy.getMaxEnergyStored() - item_energy.getEnergyStored();
    }
    return 0;
  }

  @Override
  @Nullable
  public final AbstractContainerMenu createMenu(int containerID, Inventory player_inventory, Player player){
    return new ChargerContainer(containerID, player_inventory, this);
  }

  @Override
  public final Component getDisplayName(){
    return getBlockState().getBlock().getName();
  }

}
