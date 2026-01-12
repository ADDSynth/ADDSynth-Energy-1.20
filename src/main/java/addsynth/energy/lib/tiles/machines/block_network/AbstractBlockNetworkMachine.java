package addsynth.energy.lib.tiles.machines.block_network;

import javax.annotation.Nullable;
import addsynth.core.block_network.BlockNetwork;
import addsynth.core.block_network.IBlockNetworkUser;
import addsynth.core.game.tiles.TileBase;
import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.main.IEnergyConsumer;
import addsynth.energy.lib.main.Receiver;
import addsynth.energy.lib.tiles.AbstractEnergyNetworkTile;
import addsynth.energy.lib.tiles.machines.TileAbstractMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** <p>This is a special TileEntity class for TileEntities that are part of a
 *  {@link BlockNetwork}, but are also a machine, and thus accepts energy to do work.
 *  <p>Machines which have their own BlockNetwork MUST derive from this class because they can't
 *  derive from {@link TileAbstractMachine} anymore because that class (which most other machines
 *  that don't have a BlockNetwork derive from) extends from {@link AbstractEnergyNetworkTile}
 *  and thus designed to connect to and work with the {@link EnergyNetwork}. TileEntities cannot
 *  be a part of more than one BlockNetwork.
 *  <p>Although this class provides some basic commonality for machines that use a BlockNetwork,
 *  there was some methods that I wasn't able to implement, such as the <c>serverTick()</c>
 *  and <c>getEnergy()</c> methods, so derived types will still need to implement them.
 * @param <T>
 */
// I REALLY wish I could have a generic BlockNetwork TileEntity, but I just can't figure out the generics!
public abstract class AbstractBlockNetworkMachine<T extends BlockNetwork> extends TileBase implements IBlockNetworkUser<T>, IEnergyConsumer {

  protected final Receiver energy;
  protected T network;

  public AbstractBlockNetworkMachine(BlockEntityType type, BlockPos position, BlockState blockstate, Receiver energy){
    super(type, position, blockstate);
    this.energy = energy;
  }

  @Override
  public void load(final CompoundTag tag){
    super.load(tag);
    energy.loadFromNBT(tag);
  }

  @Override
  protected void saveAdditional(final CompoundTag tag){
    super.saveAdditional(tag);
    energy.saveToNBT(tag);
  }

  @Override
  @Nullable
  public T getBlockNetwork(){
    return network;
  }

  @Override
  public void setBlockNetwork(T network){
    this.network = network;
  }

  @Override
  public double getRequestedEnergy(){
    return getEnergy().getRequestedEnergy();
  }

}
