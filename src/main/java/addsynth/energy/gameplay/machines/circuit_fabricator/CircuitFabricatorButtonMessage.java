package addsynth.energy.gameplay.machines.circuit_fabricator;

import addsynth.core.util.network.TileEntityNetworkMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class CircuitFabricatorButtonMessage extends TileEntityNetworkMessage<TileCircuitFabricator> {

  private final boolean insert;

  public CircuitFabricatorButtonMessage(final BlockPos position, final boolean insert){
    super(position, TileCircuitFabricator.class);
    this.insert = insert;
  }

  public CircuitFabricatorButtonMessage(final FriendlyByteBuf buf){
    super(buf.readBlockPos(), TileCircuitFabricator.class);
    this.insert = buf.readBoolean();
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeBoolean(insert);
  }

  @Override
  protected final void handle(final ServerLevel level, final ServerPlayer player, final TileCircuitFabricator tile){
    if(insert){
      tile.insertItems(player.getInventory());
    }
    else{
      tile.takeItems(player.getInventory());
    }
  }

}
