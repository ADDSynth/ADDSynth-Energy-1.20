package addsynth.energy.gameplay.machines.compressor;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.container.slots.OutputSlot;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class ContainerCompressor extends TileEntityContainer<TileCompressor> {

  public ContainerCompressor(final int id, final Inventory player_inventory, final TileCompressor tile){
    super(Containers.COMPRESSOR.get(), id, player_inventory, tile);
    common_setup(player_inventory);
  }

  public ContainerCompressor(final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(Containers.COMPRESSOR.get(), id, player_inventory, data);
    common_setup(player_inventory);
  }

  private final void common_setup(final Inventory player_inventory){
    make_player_inventory(player_inventory, 8, 100);
    addSlot(new  InputSlot(tile, 0,  32, 42));
    addSlot(new OutputSlot(tile, 0, 128, 42));
  }

}
