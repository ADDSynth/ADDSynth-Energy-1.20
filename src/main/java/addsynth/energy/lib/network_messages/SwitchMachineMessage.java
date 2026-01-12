package addsynth.energy.lib.network_messages;

import addsynth.core.util.network.SimpleTileEntityNetworkMessage;
import addsynth.energy.lib.gui.widgets.OnOffSwitch;
import addsynth.energy.lib.tiles.machines.switchable.ISwitchableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

/** After a player clicks on an {@link OnOffSwitch} this message gets sent to a
 *  TileEntity that implements the {@link ISwitchableMachine} interface and calls
 *  the {@link ISwitchableMachine#togglePowerSwitch() togglePowerSwitch()} method.
 */
public final class SwitchMachineMessage extends SimpleTileEntityNetworkMessage {

  public SwitchMachineMessage(final BlockPos position){
    super(position);
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
  }

  public static final SwitchMachineMessage decode(final FriendlyByteBuf buf){
    return new SwitchMachineMessage(buf.readBlockPos());
  }

  @Override
  protected final void handle(final ServerLevel level, final BlockEntity tile){
    if(tile instanceof ISwitchableMachine machine){
      machine.togglePowerSwitch();
    }
  }

}
