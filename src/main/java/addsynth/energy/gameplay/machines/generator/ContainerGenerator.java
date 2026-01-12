package addsynth.energy.gameplay.machines.generator;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.container.slots.FuelSlot;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class ContainerGenerator extends TileEntityContainer<TileGenerator> {

  public ContainerGenerator(int id, Inventory player_inventory, TileGenerator tile){
    super(Containers.GENERATOR.get(), id, player_inventory, tile);
    common_setup(player_inventory);
  }

  public ContainerGenerator(int id, Inventory player_inventory, FriendlyByteBuf data){
    super(Containers.GENERATOR.get(), id, player_inventory, data);
    common_setup(player_inventory);
  }

  private final void common_setup(final Inventory player_inventory){
    make_player_inventory(player_inventory, 12, 106);
    addSlot(new FuelSlot(tile, 0, 84, 20));
  }

}
