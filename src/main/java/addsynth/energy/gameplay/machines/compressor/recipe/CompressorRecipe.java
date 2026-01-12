package addsynth.energy.gameplay.machines.compressor.recipe;

import addsynth.core.recipe.shapeless.AbstractRecipe;
import addsynth.energy.gameplay.EnergyBlocks;
import addsynth.energy.gameplay.reference.RecipeTypes;
import addsynth.energy.registers.RecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class CompressorRecipe extends AbstractRecipe {

  public CompressorRecipe(ResourceLocation id, String group, ItemStack output, NonNullList<Ingredient> input){
    super(id, group, output, input);
  }

  @Override
  public ItemStack getToastSymbol(){
    return new ItemStack(EnergyBlocks.compressor.get(), 1);
  }

  @Override
  public RecipeSerializer<?> getSerializer(){
    return RecipeSerializers.COMPRESSOR.get();
  }

  @Override
  public RecipeType<?> getType(){
    return RecipeTypes.COMPRESSOR.get();
  }

}
