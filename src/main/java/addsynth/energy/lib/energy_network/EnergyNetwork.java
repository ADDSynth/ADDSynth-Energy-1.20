package addsynth.energy.lib.energy_network;

import java.util.HashSet;
import javax.annotation.Nullable;
import addsynth.core.block_network.BlockNetwork;
import addsynth.core.block_network.BlockNetworkHandler;
import addsynth.core.block_network.node.BlockEntityNode;
import addsynth.core.block_network.node.Node;
import addsynth.core.block_network.search.AdvancedSearchAlgorithm;
import addsynth.energy.gameplay.machines.energy_storage.TileEnergyStorage;
import addsynth.energy.gameplay.machines.energy_wire.TileEnergyWire;
import addsynth.energy.gameplay.machines.universal_energy_interface.TileUniversalEnergyInterface;
import addsynth.energy.lib.main.IEnergyUser;
import addsynth.energy.lib.tiles.AbstractEnergyNetworkTile;
import addsynth.energy.lib.tiles.AbstractEnergyTile;
import addsynth.energy.lib.tiles.generators.TileAbstractGenerator;
import addsynth.energy.lib.tiles.machines.TileAbstractMachine;
import addsynth.energy.lib.tiles.machines.block_network.AbstractBlockNetworkMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

// original inspiration from canitzp:
// https://github.com/canitzp/Metalworks/blob/master/src/main/java/de/canitzp/metalworks/block/cable/Network.java

/** The EnergyNetwork is responsible for transferring energy to and from machines. It only keeps a list
 *  of receivers, batteries, and generators, and no other data.
 *  All machines and wire are considered to be part of an energy network, except in the case where the
 *  space we searched before is a machine, and the space we're searching now is also a machine. This
 *  ensures at least 1 wire must be present to connect two machines. We also can't pass though blocks that
 *  are part of their own block network, but this is a limitation I'm prepared to accept at this time.
 */
public final class EnergyNetwork extends BlockNetwork<AbstractEnergyNetworkTile> {

  private final HashSet<EnergyNode> all_machines = new HashSet<>();
  private final EnergyNetworkData   transfer_data = new EnergyNetworkData();

  public static final BlockNetworkHandler<AbstractEnergyNetworkTile, EnergyNetwork> handler = new BlockNetworkHandler<>(AbstractEnergyNetworkTile.class, EnergyNetwork::new);

  public EnergyNetwork(final BlockPos position){
    super(position, new AdvancedSearchAlgorithm(EnergyNetwork::canNavigate));
  }

  // To handle whether a Generator is connected to 2 or more Energy Networks, or a Receiver is connected
  // to 2 or more Energy Networks, rather than trying to portion how much energy to give/take from the
  // energy networks, the whole thing should be a single energy network, which means our search algorithm
  // must 'pass through' the machine. But I still consider two machines adjacent to each other but NOT
  // connected by wire, to be NOT be connected, and thus be two separate energy networks.
  // An energy network MUST consist of all machines connected, but FAIL if going from machine to machine.
  /** This is the primary method that determines if a Node gets added to the Energy Network. */
  private static final boolean canNavigate(@Nullable final BlockEntityNode previous_node, final BlockEntityNode current_node){
    final BlockEntity tile = current_node.getTile();
    if(previous_node != null){
      final BlockEntity previous_tile = previous_node.getTile();
      // Wire/Battery -> Wire/Battery TRUE
      // Wire -> Machine   OKAY
      // Machine -> Wire   OKAY
      // Machine/Battery -> Machine FAIL
      if(isMachine(previous_tile)){
        // if prevous node was a machine, we can only navigate to wires
        return isWire(tile);
      }
      if(isBattery(previous_tile)){
        // batteries can navigate to wires or other batteries
        return isWire(tile) || isBattery(tile);
      }
    }
    return tile instanceof AbstractEnergyNetworkTile;
  }

  private static final boolean isMachine(final BlockEntity tile){
    return tile instanceof TileAbstractMachine || tile instanceof TileAbstractGenerator || tile instanceof TileUniversalEnergyInterface;
  }

  private static final boolean isBattery(final BlockEntity tile){
    return tile instanceof TileEnergyStorage;
  }

  private static final boolean isWire(final BlockEntity tile){
    return tile instanceof TileEnergyWire;
  }

  @Override
  protected void clear_custom_data(){
    all_machines.clear();
    transfer_data.clear();
  }

  @Override
  protected final void tick(final ServerLevel world){
    transfer_data.tick();
  }

  /** This is the primary method that determines if a machine gets added to the Energy Network.
   *  Remember, we allow machines to be added if the previous Node was NOT a machine */
  @Override
  protected final void customSearch(@Nullable final Node previous, final Node node, final ServerLevel world){
    @Nullable BlockEntity tile = node.getTile();
    if(tile != null){
      if(previous != null){
        @Nullable BlockEntity previous_tile = previous.getTile();
        if(previous_tile != null){
          final boolean previous_is_machine = isMachine(previous_tile);
          final boolean     tile_is_machine = isMachine(         tile);
          if(previous_is_machine && tile_is_machine){ return; }
          if(    tile_is_machine && isBattery(previous_tile)){ return; }
          if(previous_is_machine && isBattery(         tile)){ return; }
        }
      }
      // Add new machine
      if(tile instanceof AbstractEnergyTile energy_tile){
        all_machines.add(new EnergyNode<>(energy_tile));
        transfer_data.add(energy_tile);
      }
      else if(tile instanceof AbstractBlockNetworkMachine block_network_machine){
        all_machines.add(new EnergyNode<>(block_network_machine));
        transfer_data.add(block_network_machine);
      }
    }
  }

  @Override
  public void neighbor_was_changed(final ServerLevel world, final BlockPos current_position, final BlockPos position_of_neighbor){
    final BlockEntity tile = world.getBlockEntity(position_of_neighbor);
    if(tile != null){
      if(tile instanceof IEnergyUser){
        updateBlockNetwork(world, current_position);
      }
    }
  }

  public final EnergyNode[] getDiagnosticsData(){
    return all_machines.toArray(new EnergyNode[all_machines.size()]);
  }

}
