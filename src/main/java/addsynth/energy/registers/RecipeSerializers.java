package addsynth.energy.registers;

import addsynth.energy.gameplay.machines.circuit_fabricator.recipe.CircuitFabricatorRecipe;
import addsynth.energy.gameplay.machines.compressor.recipe.CompressorRecipe;
import addsynth.energy.gameplay.reference.Names;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class RecipeSerializers {

  public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR =
    RegistryObject.create(Names.COMPRESSOR, ForgeRegistries.RECIPE_SERIALIZERS);

  public static final RegistryObject<RecipeSerializer<CircuitFabricatorRecipe>> CIRCUIT_FABRICATOR =
    RegistryObject.create(Names.CIRCUIT_FABRICATOR, ForgeRegistries.RECIPE_SERIALIZERS);

}
