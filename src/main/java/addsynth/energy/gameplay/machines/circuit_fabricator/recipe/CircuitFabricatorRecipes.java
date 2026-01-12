package addsynth.energy.gameplay.machines.circuit_fabricator.recipe;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import addsynth.core.recipe.RecipeCollection;
import addsynth.energy.ADDSynthEnergy;
import addsynth.energy.gameplay.EnergyItems;
import addsynth.energy.gameplay.machines.circuit_fabricator.CircuitFabricatorGui;
import addsynth.energy.gameplay.reference.RecipeTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CircuitFabricatorRecipes {

  public static final RecipeCollection<CircuitFabricatorRecipe> INSTANCE =
    new RecipeCollection<CircuitFabricatorRecipe>(RecipeTypes.CIRCUIT_FABRICATOR, 8);

  /** This is used by the {@link CircuitFabricatorGui} to populate the Item list. */
  public static final ItemStack[] getRecipes(){
    // add circuits first
    final ArrayList<ItemStack> output = new ArrayList<>(30);
    output.add(new ItemStack(EnergyItems.circuit_tier_1.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_2.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_3.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_4.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_5.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_6.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_7.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_8.get()));
    output.add(new ItemStack(EnergyItems.circuit_tier_9.get()));
    // add all other items
    final ArrayList<CircuitFabricatorRecipe> recipes = new ArrayList<>(INSTANCE.getRecipes());
    recipes.removeIf(
      (CircuitFabricatorRecipe r) -> {
        return r.getId().getNamespace().equals(ADDSynthEnergy.MOD_ID);
      }
    );
    
    final BiConsumer<ArrayList<ItemStack>, ItemStack> add = (ArrayList<ItemStack> list, ItemStack result) -> {
      final Item result_item = result.getItem();
      Item item;
      for(ItemStack i : list){
        item = i.getItem();
        if(item == result_item){
          return; // item already exists in list, so return and don't do anything
        }
      }
      list.add(result);
    };
    
    for(CircuitFabricatorRecipe recipe : recipes){
      add.accept(output, recipe.getResultItem());
    }
    return output.toArray(new ItemStack[output.size()]);
  }

}
