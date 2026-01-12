package addsynth.energy.gameplay.machines.circuit_fabricator;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.container.slots.OutputSlot;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class CircuitFabricatorContainer extends TileEntityContainer<TileCircuitFabricator> {

  public CircuitFabricatorContainer(int id, Inventory player_inventory, FriendlyByteBuf data){
    super(Containers.CIRCUIT_FABRICATOR.get(), id, player_inventory, data);
    common_setup(player_inventory);
  }

  public CircuitFabricatorContainer(int id, Inventory player_inventory, TileCircuitFabricator tile){
    super(Containers.CIRCUIT_FABRICATOR.get(), id, player_inventory, tile);
    common_setup(player_inventory);
  }

  private final void common_setup(final Inventory player_inventory){
    make_player_inventory(player_inventory, 87, 157);
    addSlot(new  InputSlot(tile, 0, 162, 76));
    addSlot(new  InputSlot(tile, 1, 180, 76));
    addSlot(new  InputSlot(tile, 2, 198, 76));
    addSlot(new  InputSlot(tile, 3, 216, 76));
    addSlot(new  InputSlot(tile, 4, 162, 94));
    addSlot(new  InputSlot(tile, 5, 180, 94));
    addSlot(new  InputSlot(tile, 6, 198, 94));
    addSlot(new  InputSlot(tile, 7, 216, 94));
    addSlot(new OutputSlot(tile, 0, 268, 85));
  }

}
