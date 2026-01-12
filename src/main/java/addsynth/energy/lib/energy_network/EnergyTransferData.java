package addsynth.energy.lib.energy_network;

import addsynth.core.util.java.list.IndexedSet;
import addsynth.energy.lib.main.IEnergyUser;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Energy Transfer Data is simply a class that stores all the machine's energy values
 *  and they are only acquired once at the beginning of the energy tick and then
 *  modified during each transfer step, instead of re-acquiring on each transfer.
 * @param <T>
 */
public abstract class EnergyTransferData<T extends BlockEntity & IEnergyUser> {

  protected int size;
  protected final IndexedSet<EnergyNode<T>> list = new IndexedSet<>();

  public abstract void update();

  public final void clear(){
    list.clear();
  }

  public final void add(final T tile){
    list.add(new EnergyNode<>(tile));
  }

  public final int size(){
    return size;
  }

  @Override
  public String toString(){
    return list.toString();
  }

}
