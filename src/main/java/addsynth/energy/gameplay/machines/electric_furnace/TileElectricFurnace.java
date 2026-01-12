package addsynth.energy.gameplay.machines.electric_furnace;

import javax.annotation.Nullable;
import addsynth.core.recipe.FurnaceRecipes;
import addsynth.energy.lib.tiles.machines.TileStandardWorkMachine;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class TileElectricFurnace extends TileStandardWorkMachine implements MenuProvider {

  public TileElectricFurnace(BlockPos position, BlockState blockstate){
    super(Tiles.ELECTRIC_FURNACE.get(), position, blockstate, 1, FurnaceRecipes.INSTANCE.getFilter(), 1);
    inventory.setRecipeProvider(FurnaceRecipes.INSTANCE);
    energy.setMaxReceive(5);
  }

  @Override
  protected final void begin_work(){
    inventory.begin_work();
    // factor in burn time of item being smelted
    final ItemStack stack = inventory.getWorkingInventory().getStackInSlot(0);
    final int smelting_time = FurnaceRecipes.getSmeltingTime(stack);
    energy.setCapacity(smelting_time * 5); // 1 smelting operator is 200 ticks (1,000 energy)
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, Inventory player_inventory, Player player){
    return new ContainerElectricFurnace(id, player_inventory, this);
  }

  @Override
  public Component getDisplayName(){
    return getBlockState().getBlock().getName();
  }

}
