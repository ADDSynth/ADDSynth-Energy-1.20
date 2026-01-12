package addsynth.energy.lib.tiles.generators;

import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.main.Generator;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** This is the standard Generator implementation which directly extends from
 *  {@link TileAbstractGenerator} and has all basic Generator functions. This
 *  is a seperate class to differentiate it from {@link TilePassiveGenerator}.
 *  This is not directly being used right now, because most Generators
 *  generate energy based on some {@link TileInputGenerator input items}.
 */
public abstract class TileStandardGenerator extends TileAbstractGenerator {

  protected final Generator energy = new Generator();
  protected boolean changed;

  public TileStandardGenerator(final BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
  }

  @Override
  public final void serverTick(ServerLevel level, BlockState blockstate){
    EnergyNetwork.handler.tick(network, level, this);
    derivedTick(level, blockstate);
    if(energy.tick()){
      changed = true;
    }
    if(changed){
      update_data();
      changed = false;
    }
  }

  protected abstract void derivedTick(ServerLevel level, BlockState blockstate);

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    energy.loadFromNBT(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    energy.saveToNBT(nbt);
  }

  protected abstract void setGeneratorData();

  @Override
  public double getAvailableEnergy(){
    return energy.getAvailableEnergy();
  }

  @Override
  public final Generator getEnergy(){
    return energy;
  }

}
