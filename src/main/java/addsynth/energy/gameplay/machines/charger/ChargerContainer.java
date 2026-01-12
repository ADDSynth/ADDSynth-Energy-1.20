package addsynth.energy.gameplay.machines.charger;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.container.slots.OutputSlot;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class ChargerContainer extends TileEntityContainer<TileCharger> {

  public ChargerContainer(int id, Inventory player_inventory, TileCharger tile){
    super(Containers.CHARGER.get(), id, player_inventory, tile);
    common_setup(player_inventory);
  }

  public ChargerContainer(int id, Inventory player_inventory, FriendlyByteBuf data){
    super(Containers.CHARGER.get(), id, player_inventory, data);
    common_setup(player_inventory);
  }

  private final void common_setup(final Inventory player_inventory){
    make_player_inventory(player_inventory, 8, 90);
    addSlot(new  InputSlot(tile, 0,  56, 40));
    addSlot(new OutputSlot(tile, 0, 104, 40));
  }

}
