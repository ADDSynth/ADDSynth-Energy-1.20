package addsynth.energy.lib.tiles.generators;

import java.util.function.Predicate;
import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.InputInventory;
import addsynth.core.game.inventory.InventoryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Standard Generators are generators that have an Input inventory and presumably
 *  consume fuel to produce Energy. Logic still needs to be specified however.
 * @author ADDSynth
 */
public abstract class TileInputGenerator extends TileStandardGenerator implements IInputInventory {

  protected final InputInventory input_inventory;

  public TileInputGenerator(final BlockEntityType type, BlockPos position, BlockState blockstate, final Predicate<ItemStack> filter){
    super(type, position, blockstate);
    this.input_inventory = InputInventory.create(this, 1, filter);
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    input_inventory.load(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    input_inventory.save(nbt);
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side){
    if(remove == false){
      if(capability == ForgeCapabilities.ITEM_HANDLER){
        return InventoryUtil.getInventoryCapability(input_inventory, null, side);
      }
      return super.getCapability(capability, side);
    }
    return LazyOptional.empty();
  }

  @Override
  public void onInventoryChanged(){
    changed = true;
  }

  @Override
  public final void drop_inventory(){
    input_inventory.drop_in_world(level, worldPosition);
  }

  @Override
  public final InputInventory getInputInventory(){
    return input_inventory;
  }

}
