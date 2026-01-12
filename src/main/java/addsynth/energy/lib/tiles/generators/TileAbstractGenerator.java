package addsynth.energy.lib.tiles.generators;

import addsynth.energy.lib.main.IEnergyGenerator;
import addsynth.energy.lib.tiles.AbstractEnergyTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** All TileEntities that derive from this will be treated as a Geneartor by the Energy Network. */
public abstract class TileAbstractGenerator extends AbstractEnergyTile implements IEnergyGenerator {

  public TileAbstractGenerator(final BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
  }

}
