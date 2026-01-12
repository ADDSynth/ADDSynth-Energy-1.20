package addsynth.energy.gameplay.reference;

import addsynth.energy.gameplay.machines.circuit_fabricator.recipe.CircuitFabricatorRecipe;
import addsynth.energy.gameplay.machines.compressor.recipe.CompressorRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class RecipeTypes {

  public static final RegistryObject<RecipeType<CircuitFabricatorRecipe>> CIRCUIT_FABRICATOR = RegistryObject.create(Names.CIRCUIT_FABRICATOR, ForgeRegistries.RECIPE_TYPES);
  public static final RegistryObject<RecipeType<CompressorRecipe>> COMPRESSOR = RegistryObject.create(Names.COMPRESSOR, ForgeRegistries.RECIPE_TYPES);

}
