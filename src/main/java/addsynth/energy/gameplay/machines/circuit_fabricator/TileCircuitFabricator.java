package addsynth.energy.gameplay.machines.circuit_fabricator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import addsynth.core.game.inventory.filter.RecipeFilter;
import addsynth.core.util.game.data.NBTUtil;
import addsynth.energy.ADDSynthEnergy;
import addsynth.energy.gameplay.config.Config;
import addsynth.energy.gameplay.machines.circuit_fabricator.recipe.CircuitFabricatorRecipe;
import addsynth.energy.gameplay.machines.circuit_fabricator.recipe.CircuitFabricatorRecipes;
import addsynth.energy.gameplay.reference.Names;
import addsynth.energy.lib.tiles.machines.TileStandardWorkMachine;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public final class TileCircuitFabricator extends TileStandardWorkMachine implements MenuProvider {

  private static final ResourceLocation defaultRecipe = Names.CIRCUIT_TIER_1;
  @Nonnull
  private ResourceLocation output_itemStack = defaultRecipe;
  // This RecipeFilter is different for every machine, therefore, it SHOULD NOT BE STATIC.
  private final RecipeFilter filter = new RecipeFilter(8);
  private static final String saveTag = "Recipe";

  public TileCircuitFabricator(BlockPos position, BlockState blockstate){
    super(Tiles.CIRCUIT_FABRICATOR.get(), position, blockstate, 8, null, 1, Config.circuit_fabricator);
    inventory.getInputInventory().isItemStackValid = filter::test;
    inventory.setRecipeProvider(CircuitFabricatorRecipes.INSTANCE);
    rebuild_filters(); // sets default filter for new TileEntities.
  }

  public final void change_recipe(final ResourceLocation new_recipe){
    if(output_itemStack.equals(new_recipe) == false){
     output_itemStack = new_recipe;
     rebuild_filters();
     changed = true;
    }
  }

  public final void rebuild_filters(){
    // find recipe
    final CircuitFabricatorRecipe recipe = CircuitFabricatorRecipes.INSTANCE.find_recipe(output_itemStack);
    if(recipe != null){
      filter.set(recipe);
      // update recipe in gui if on client side
      updateGui();
    }
    else{
      // Handle invalid recipe
      ADDSynthEnergy.log.warn("Circuit Fabricator recipe for "+output_itemStack.toString()+" doesn't exist anymore.");
      // PRIORITY: add a resetMachine() function. Add a call here. Check how we currently handle unexpected machine state errors.
      // Pop out the items in the working inventory.
      change_recipe(defaultRecipe);
    }
  }

  /** Go through all inventory slots and eject all items that don't match.
   *  This can only be called when the player clicks on a change recipe button on the gui,
   *  which then sends a network message to the server.
   */
  public final void ejectInvalidItems(final Player player){
    inventory.getInputInventory().ejectInvalidItems(player);
  }

  @SuppressWarnings("null")
  public final void updateGui(){
    if(level != null){
      if(level.isClientSide){
        CircuitFabricatorGui.updateRecipeDisplay(filter.getIngredients());
      }
    }
  }

  public final ItemStack getRecipeOutput(){
    return new ItemStack(ForgeRegistries.ITEMS.getValue(output_itemStack));
  }

  @Override
  protected final void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    nbt.putString(saveTag, output_itemStack.toString());
  }

  @Override
  public final void load(final CompoundTag nbt){
    super.load(nbt);
    change_recipe(NBTUtil.loadResourceLocationAndCheckItem(nbt, saveTag, defaultRecipe));
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, Inventory player_inventory, Player player){
    return new CircuitFabricatorContainer(id, player_inventory, this);
  }

  @Override
  public Component getDisplayName(){
    return getBlockState().getBlock().getName();
  }

}
