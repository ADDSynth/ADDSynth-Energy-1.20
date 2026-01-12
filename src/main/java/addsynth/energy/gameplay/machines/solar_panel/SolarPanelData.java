package addsynth.energy.gameplay.machines.solar_panel;

import addsynth.core.util.network.TileEntityClientMessage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

/** Since all solar panel data can be calculated on the server, there's no need to save
 *  anything. Send all relevant data to the client. */
public final class SolarPanelData extends TileEntityClientMessage<SolarPanelControllerTile> {

  public SolarPanelStatus status;
  public int daytime;
  public double phase;
  public double energy;
  public int panel_count;
  public int blocked_count;
  public double theoretical_energy;

  public SolarPanelData(BlockPos position){
    super(position, SolarPanelControllerTile.class);
  }

  public SolarPanelData(final FriendlyByteBuf buf){
    super(buf.readBlockPos(), SolarPanelControllerTile.class);
    status = buf.readEnum(SolarPanelStatus.class);
    daytime = buf.readInt();
    phase = buf.readDouble();
    energy = buf.readDouble();
    panel_count = buf.readInt();
    blocked_count = buf.readInt();
    theoretical_energy = buf.readDouble();
  }

  public final void set(SolarPanelStatus status, int daytime, double phase, double energy, int panel_count, int blocked_count, double efficiency){
    this.status = status;
    this.daytime = daytime;
    this.phase = phase;
    this.energy = energy;
    this.panel_count = panel_count;
    this.blocked_count = blocked_count;
    this.theoretical_energy = efficiency;
  }

  @Override
  public final void encode(FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    buf.writeEnum(status);
    buf.writeInt(daytime);
    buf.writeDouble(phase);
    buf.writeDouble(energy);
    buf.writeInt(panel_count);
    buf.writeInt(blocked_count);
    buf.writeDouble(theoretical_energy);
  }

  @Override
  protected final void handle(final ClientLevel level, final LocalPlayer player, final SolarPanelControllerTile tile){
    tile.setFromServer(this);
  }

}
