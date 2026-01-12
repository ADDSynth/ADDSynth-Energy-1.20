package addsynth.energy.lib.network_messages;

import addsynth.core.util.network.TileEntityClientMessage;
import addsynth.energy.lib.tiles.machines.MachineStatus;
import addsynth.energy.lib.tiles.machines.TileAbstractWorkMachine;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

/** I now see why there are separate methods for saving TileEntity data to disk, and sending data to
 *  the Client. Sometimes there is data that can be calculated on the server and sent to the Client,
 *  there's no need to save it. This network message is temporary. TileBase, and TileBaseNoData will
 *  be deleted in the future.
 */
public class UpdateClientMachineStatusMessage extends TileEntityClientMessage<TileAbstractWorkMachine> {

  private int status;

  public UpdateClientMachineStatusMessage(BlockPos position, MachineStatus status){
    super(position, TileAbstractWorkMachine.class);
    this.status = status.ordinal();
  }

  public UpdateClientMachineStatusMessage(FriendlyByteBuf buf){
    super(buf.readBlockPos(), TileAbstractWorkMachine.class);
    this.status = buf.readInt();
  }

  @Override
  public void encode(FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeInt(status);
  }

  @Override
  protected void handle(ClientLevel level, LocalPlayer player, TileAbstractWorkMachine tile){
    tile.setStatus(MachineStatus.values()[status]);
  }

}
