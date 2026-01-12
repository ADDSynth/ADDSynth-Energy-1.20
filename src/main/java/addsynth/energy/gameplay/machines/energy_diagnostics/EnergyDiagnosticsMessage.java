package addsynth.energy.gameplay.machines.energy_diagnostics;

import java.util.Collection;
import addsynth.core.util.network.TileEntityClientMessage;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public final class EnergyDiagnosticsMessage extends TileEntityClientMessage<TileEnergyDiagnostics> {

  private final int number_of_machine_data;
  private final EnergyDiagnosticData[] diagnostic_data;
  private final EnergyDiagnosticData totals;

  public EnergyDiagnosticsMessage(BlockPos position){
    super(position, TileEnergyDiagnostics.class);
    number_of_machine_data = -1;
    diagnostic_data = null;
    totals = null;
  }

  public EnergyDiagnosticsMessage(BlockPos position, Collection<EnergyDiagnosticData> diagnostic_data, EnergyDiagnosticData totals){
    super(position, TileEnergyDiagnostics.class);
    number_of_machine_data = diagnostic_data.size();
    this.diagnostic_data = diagnostic_data.toArray(new EnergyDiagnosticData[number_of_machine_data]);
    this.totals = totals;
  }

  public EnergyDiagnosticsMessage(final FriendlyByteBuf buf){
    super(buf.readBlockPos(), TileEnergyDiagnostics.class);
    number_of_machine_data = buf.readInt();
    if(number_of_machine_data >= 0){
      diagnostic_data = new EnergyDiagnosticData[number_of_machine_data];
      int i;
      for(i = 0; i < number_of_machine_data; i++){
        diagnostic_data[i] = new EnergyDiagnosticData(buf);
      }
      totals = new EnergyDiagnosticData(buf);
    }
    else{
      diagnostic_data = null;
      totals = null;
    }
  }

  @Override
  public final void encode(final FriendlyByteBuf buf){
    buf.writeBlockPos(position);
    if(diagnostic_data != null && number_of_machine_data >= 0){
      buf.writeInt(number_of_machine_data);
      for(EnergyDiagnosticData data : diagnostic_data){
        data.save(buf);
      }
      totals.save(buf);
    }
    else{
      buf.writeInt(-1);
    }
  }

  @Override
  protected final void handle(final ClientLevel level, final LocalPlayer player, final TileEnergyDiagnostics diagnostics_machine){
    diagnostics_machine.set(diagnostic_data, totals);
  }

}
