package addsynth.energy.gameplay.machines.energy_wire;

import addsynth.energy.lib.tiles.AbstractEnergyNetworkTile;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This is the TileEntity that Energy Wires should use to form an Energy Network.
 * Have your Wire blocks create a new TileEntity instance of this class type.
 * This class and Energy Network will automatically detect the Block type and
 * connect to other blocks of the same type.
 * @author ADDSynth
 */
public final class TileEnergyWire extends AbstractEnergyNetworkTile {

  public TileEnergyWire(BlockPos position, BlockState blockstate){
    super(Tiles.ENERGY_WIRE.get(), position, blockstate);
  }

}
