package addsynth.energy.gameplay.machines.universal_energy_interface;

import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SetTransferSettings extends TileEntityNetworkMessage<TileUniversalEnergyInterface> {

  private final int index;

  public SetTransferSettings(final TileUniversalEnergyInterface tile, final int index){
    super(tile.getBlockPos(), TileUniversalEnergyInterface.class);
    this.index = index;
  }

  public SetTransferSettings(final FriendlyByteBuf buf){
    super(buf.readBlockPos(), TileUniversalEnergyInterface.class);
    this.index = buf.readInt();
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeInt(index);
  }

  @Override
  public final void handle(final ServerLevel level, final ServerPlayer player, final TileUniversalEnergyInterface tile){
    tile.setTransferSettings(index);
  }

}
