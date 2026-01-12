package addsynth.energy.lib.tiles.battery;

import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.main.Energy;
import addsynth.energy.lib.tiles.AbstractEnergyTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** This is the abstract class for all TileEntities that can both extract and receive
 *  energy. This does not necessarily mean this is the abstract class for all Batteries.
 */
public abstract class BasicEnergyTile extends AbstractEnergyTile {

  protected final Energy energy;
  protected boolean changed;

  public BasicEnergyTile(final BlockEntityType type, BlockPos position, BlockState blockstate){
    super(type, position, blockstate);
    this.energy = new Energy();
  }

  public BasicEnergyTile(final BlockEntityType type, BlockPos position, BlockState blockstate, final Energy energy){
    super(type, position, blockstate);
    this.energy = energy;
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

  protected void derivedTick(ServerLevel level, BlockState blockstate){
  }

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

  @Override
  public final Energy getEnergy(){
    return energy;
  }

}
