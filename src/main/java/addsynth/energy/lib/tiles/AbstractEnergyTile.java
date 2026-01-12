package addsynth.energy.lib.tiles;

import addsynth.energy.lib.main.IEnergyUser;
import addsynth.energy.lib.tiles.battery.TileEnergyBattery;
import addsynth.energy.lib.tiles.generators.TileAbstractGenerator;
import addsynth.energy.lib.tiles.machines.TileAbstractMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** All TileEntities that use Energy must derive from this class.
 *  @see TileAbstractGenerator
 *  @see TileAbstractMachine
 *  @see TileEnergyBattery
 */
public abstract class AbstractEnergyTile extends AbstractEnergyNetworkTile implements IEnergyUser {

  public AbstractEnergyTile(BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
  }

}
