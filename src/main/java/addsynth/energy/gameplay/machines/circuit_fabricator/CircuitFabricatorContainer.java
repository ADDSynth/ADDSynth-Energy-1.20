package addsynth.energy.gameplay.machines.circuit_fabricator;

import addsynth.core.container.TileEntityContainer;
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
    make_player_inventory(player_inventory, 169, 157);
    addInputSlots(tile, 0, 177, 56, 8, 1);
    addSlot(new OutputSlot(tile, 0, 292, 91));
  }

}
