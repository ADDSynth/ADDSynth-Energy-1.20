package addsynth.energy.gameplay.machines.energy_storage;

import addsynth.core.container.TileEntityContainer;
import addsynth.energy.registers.Containers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public final class ContainerEnergyStorage extends TileEntityContainer<TileEnergyStorage> {

  public ContainerEnergyStorage(final int id, final Inventory player_inventory, final TileEnergyStorage tile){
    super(Containers.ENERGY_STORAGE_CONTAINER.get(), id, player_inventory, tile);
  }

  public ContainerEnergyStorage(final int id, final Inventory player_inventory, final FriendlyByteBuf data){
    super(Containers.ENERGY_STORAGE_CONTAINER.get(), id, player_inventory, data);
  }

}
