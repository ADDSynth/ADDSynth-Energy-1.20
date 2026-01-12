package addsynth.energy.lib.network_messages;

import addsynth.core.util.network.SimpleTileEntityNetworkMessage;
import addsynth.energy.lib.tiles.machines.switchable.IAutoShutoff;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ToggleAutoShutoffMessage extends SimpleTileEntityNetworkMessage {

  public ToggleAutoShutoffMessage(final BlockPos position){
    super(position);
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
  }

  public static final ToggleAutoShutoffMessage decode(final FriendlyByteBuf buf){
    return new ToggleAutoShutoffMessage(buf.readBlockPos());
  }

  @Override
  protected final void handle(final ServerLevel level, final BlockEntity tile){
    if(tile instanceof IAutoShutoff machine){
      machine.toggle_auto_shutoff();
    }
  }

}
