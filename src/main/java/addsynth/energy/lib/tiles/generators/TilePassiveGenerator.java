package addsynth.energy.lib.tiles.generators;

import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.main.PassiveGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** This TileEntity is for machines that generate Energy all on their own
 *  based on some internal state. Passive energy DOES NOT NEED TO BE SAVED. */
public abstract class TilePassiveGenerator extends TileAbstractGenerator {

  protected final PassiveGenerator energy = new PassiveGenerator();

  public TilePassiveGenerator(final BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
  }

  @Override
  public final void serverTick(ServerLevel level, BlockState blockstate){
    EnergyNetwork.handler.tick(network, level, this);
    derivedTick(level, blockstate);
  }

  protected void derivedTick(ServerLevel level, BlockState blockstate){
  }

  @Override
  public double getAvailableEnergy(){
    return energy.getAvailableEnergy();
  }

  @Override
  public final PassiveGenerator getEnergy(){
    return energy;
  }

  @Override
  public boolean isFreeEnergy(){
    return true;
  }

}
