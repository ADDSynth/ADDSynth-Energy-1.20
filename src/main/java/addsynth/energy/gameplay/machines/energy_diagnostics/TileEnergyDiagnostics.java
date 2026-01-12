package addsynth.energy.gameplay.machines.energy_diagnostics;

import java.util.ArrayList;
import addsynth.core.game.tiles.TileBaseNoData;
import addsynth.core.util.game.tileentity.ITickingTileEntity;
import addsynth.core.util.network.NetworkUtil;
import addsynth.energy.gameplay.NetworkHandler;
import addsynth.energy.gameplay.machines.energy_wire.TileEnergyWire;
import addsynth.energy.gameplay.reference.EnergyText;
import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.energy_network.EnergyNode;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class TileEnergyDiagnostics extends TileBaseNoData implements ITickingTileEntity {

  /** The gui uses this to draw the data. */
  public boolean network_exists = false;
  public final ArrayList<EnergyDiagnosticData> diagnostics_data = new ArrayList<>(20);
  public final EnergyDiagnosticData totals = new EnergyDiagnosticData(EnergyText.total);

  public TileEnergyDiagnostics(BlockPos position, BlockState blockstate){
    super(Tiles.ENERGY_DIAGNOSTICS_BLOCK.get(), position, blockstate);
  }

  @Override
  public final void serverTick(ServerLevel level, BlockState blockstate){
    // Impossible to access the Energy Network on the client side, because it doesn't exist.
    // So we need to access it on the server side, get all the variables, then send them to the clients.
    
    // find first Energy Network
    BlockEntity tile;
    for(final Direction direction : Direction.values()){
      
      tile = level.getBlockEntity(worldPosition.relative(direction));
      if(tile != null){
        if(tile instanceof TileEnergyWire energy_wire){
  
          final EnergyNetwork network = energy_wire.getBlockNetwork();
          if(network != null){
          
            // get Diagnostic Data
            final EnergyNode[] machines = network.getDiagnosticsData();
            final int length = machines.length;
            
            // clear totals
            totals.clear();
            
            adjustLength(length);
            
            // set all diagnostics data
            int i;
            EnergyNode node;
            EnergyDiagnosticData diag_data;
            for(i = 0; i < length; i++){
              node = machines[i];
              diag_data = diagnostics_data.get(i);
              diag_data.set(node.getTile(), node.getEnergy());
              totals.energy       += diag_data.energy;
              totals.capacity     += diag_data.capacity;
              totals.in           += diag_data.in;
              totals.max_receive  += diag_data.max_receive;
              totals.out          += diag_data.out;
              totals.max_transmit += diag_data.max_transmit;
              totals.transfer     += diag_data.transfer;
            }
   
            // sync data to clients near the Diagnostics machine
            NetworkUtil.send_to_TileEntity(NetworkHandler.INSTANCE, this, new EnergyDiagnosticsMessage(worldPosition, diagnostics_data, totals));
            return;
          }
        }
      }
    }
    
    // send empty network message since we didn't find an Energy Network.
    NetworkUtil.send_to_TileEntity(NetworkHandler.INSTANCE, this, new EnergyDiagnosticsMessage(worldPosition));
  }

  private final void adjustLength(int length){
    // if total machines is greater, add more objects
    if(length > diagnostics_data.size()){
      int needed = length - diagnostics_data.size();
      do{
        diagnostics_data.add(new EnergyDiagnosticData());
        needed--;
      }
      while(needed > 0);
    }
    
    // if there are more than needed, delete some
    if(length < diagnostics_data.size()){
      int remove = diagnostics_data.size() - length;
      do{
        diagnostics_data.remove(diagnostics_data.size()-1);
        remove--;
      }
      while(remove > 0);
    }
  }

  public final void set(final EnergyDiagnosticData[] diagnostic_data, EnergyDiagnosticData totals){
    network_exists = diagnostic_data != null;
    if(network_exists){
    
      @SuppressWarnings("null")
      final int length = diagnostic_data.length;
      
      adjustLength(length);
      
      int i;
      for(i = 0; i < length; i++){
        this.diagnostics_data.get(i).set(diagnostic_data[i]);
      }
      
      this.diagnostics_data.sort(null);
      
      this.totals.set(totals);
    }
  }

}
