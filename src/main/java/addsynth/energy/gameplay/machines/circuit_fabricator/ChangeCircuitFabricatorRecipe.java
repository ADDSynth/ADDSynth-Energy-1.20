package addsynth.energy.gameplay.machines.circuit_fabricator;

import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class ChangeCircuitFabricatorRecipe extends TileEntityNetworkMessage<TileCircuitFabricator> {

  private final ResourceLocation recipe_output;

  public ChangeCircuitFabricatorRecipe(final BlockPos position, final ItemStack itemstack){
    super(position, TileCircuitFabricator.class);
    this.recipe_output = ForgeRegistries.ITEMS.getKey(itemstack.getItem());
  }

  public ChangeCircuitFabricatorRecipe(final FriendlyByteBuf buf){
    super(buf.readBlockPos(), TileCircuitFabricator.class);
    this.recipe_output = ResourceLocation.parse(buf.readUtf());
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeUtf(recipe_output.toString());
  }

  @Override
  protected final void handle(final ServerLevel level, final ServerPlayer player, final TileCircuitFabricator tile){
    tile.change_recipe(recipe_output);
    tile.ejectInvalidItems(player);
  }

}
