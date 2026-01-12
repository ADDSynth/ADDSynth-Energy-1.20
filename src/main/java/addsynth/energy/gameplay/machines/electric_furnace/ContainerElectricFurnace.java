package addsynth.energy.gameplay.machines.electric_furnace;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.container.slots.InputSlot;
import addsynth.core.container.slots.OutputSlot;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class ContainerElectricFurnace extends TileEntityContainer<TileElectricFurnace> {

  public ContainerElectricFurnace(final int id, final Inventory player_inventory, final TileElectricFurnace tile){
    super(Containers.ELECTRIC_FURNACE.get(), id, player_inventory, tile);
    common_setup(player_inventory);
  }

  public ContainerElectricFurnace(final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(Containers.ELECTRIC_FURNACE.get(), id, player_inventory, data);
    common_setup(player_inventory);
  }

  private final void common_setup(final Inventory player_inventory){
    make_player_inventory(player_inventory, 8, 90);
    addSlot(new  InputSlot(tile, 0,  32, 40));
    addSlot(new OutputSlot(tile, 0, 128, 40));
  }

}
