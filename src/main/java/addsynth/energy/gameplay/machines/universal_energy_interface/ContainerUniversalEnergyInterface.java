package addsynth.energy.gameplay.machines.universal_energy_interface;

import addsynth.core.container.TileEntityContainer;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class ContainerUniversalEnergyInterface extends TileEntityContainer<TileUniversalEnergyInterface> {

  public ContainerUniversalEnergyInterface(final int id, final Inventory player_inventory, final TileUniversalEnergyInterface tile){
    super(Containers.UNIVERSAL_ENERGY_INTERFACE.get(), id, player_inventory, tile);
  }

  public ContainerUniversalEnergyInterface(final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(Containers.UNIVERSAL_ENERGY_INTERFACE.get(), id, player_inventory, data);
  }

}
