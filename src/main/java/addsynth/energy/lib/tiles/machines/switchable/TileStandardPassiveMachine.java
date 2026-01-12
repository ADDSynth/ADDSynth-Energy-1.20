package addsynth.energy.lib.tiles.machines.switchable;

import addsynth.core.game.inventory.IOutputInventory;
import addsynth.core.game.inventory.OutputInventory;
import addsynth.energy.lib.config.MachineData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** The StandardPassiveMachine is a machine that accepts energy when RUNNING and,
 *  when work is finished, creates a new Item and puts it in an {@link OutputInventory}.
 */
public abstract class TileStandardPassiveMachine extends TilePassiveMachine implements IOutputInventory {

  protected final OutputInventory output_inventory;

  public TileStandardPassiveMachine(BlockEntityType type, BlockPos position, BlockState blockstate, MachineData data, int output_slots){
    super(type, position, blockstate, data);
    output_inventory = OutputInventory.create(this, output_slots);
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    output_inventory.load(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    output_inventory.save(nbt);
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side){
    if(!remove){
      if(capability == ForgeCapabilities.ITEM_HANDLER){
        if(side != null){
          if(side == Direction.DOWN){
            return LazyOptional.of(() -> output_inventory).cast();
          }
        }
      }
      return super.getCapability(capability, side);
    }
    return LazyOptional.empty();
  }

  @Override
  public final void onInventoryChanged(){
    changed = true;
  }

  @Override
  public final void drop_inventory(){
    output_inventory.drop_in_world(level, worldPosition);
  }

  @Override
  public final OutputInventory getOutputInventory(){
    return output_inventory;
  }

}
