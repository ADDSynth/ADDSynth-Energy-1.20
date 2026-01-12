package addsynth.energy.lib.tiles.generators;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/** Fuel Generators are generators that have an Input inventory and
 *  accept fuel items to be consumed to produce Energy.
 * @author ADDSynth
 */
public abstract class TileFuelGenerator extends TileInputGenerator {

  public TileFuelGenerator(final BlockEntityType type, BlockPos position, BlockState blockstate, final Predicate<ItemStack> filter){
    super(type, position, blockstate, filter);
  }

  @Override
  protected void derivedTick(ServerLevel level, BlockState blockstate){
    // if there's any energy left, subtract available energy before we reset the IO
    energy.subtractAvailableEnergy();
    // standard generator behaviour
    if(energy.isEmpty() && !input_inventory.isEmpty()){
      setGeneratorData();
      changed = true;
    }
  }

  @Override
  public final boolean isFreeEnergy(){
    return false;
  }

}
