package addsynth.energy.lib.tiles.machines.block_network;

import java.util.function.Predicate;
import addsynth.core.block_network.BlockNetwork;
import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.InputInventory;
import addsynth.core.game.inventory.InventoryUtil;
import addsynth.core.game.inventory.SlotData;
import addsynth.energy.lib.main.Receiver;
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

/** Like the name implies, this is a BlockNetwork machine with an Input Inventory, and this
 *  class contains all the code needed for the Input Inventory to function properly.
 * @param <B>
 */
public abstract class BlockNetworkMachineWithInventory<B extends BlockNetwork> extends AbstractBlockNetworkMachine<B> implements IInputInventory {

  protected boolean changed;
  protected final InputInventory inventory;

  public BlockNetworkMachineWithInventory(BlockEntityType type, BlockPos position, BlockState blockstate,
                          SlotData[] slots, Receiver energy){
    super(type, position, blockstate, energy);
    this.inventory = InputInventory.create(this, slots);
  }

  public BlockNetworkMachineWithInventory(BlockEntityType type, BlockPos position, BlockState blockstate,
                          int input_slots, Predicate<ItemStack> filter, Receiver energy){
    super(type, position, blockstate, energy);
    this.inventory = InputInventory.create(this, input_slots, filter);
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    inventory.load(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    inventory.save(nbt);
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side){
    if(remove == false){
      if(capability == ForgeCapabilities.ITEM_HANDLER){
        return InventoryUtil.getInventoryCapability(inventory, null, side);
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
  public void drop_inventory(){
    InventoryUtil.drop_inventories(worldPosition, level, inventory);
  }

  @Override
  public InputInventory getInputInventory(){
    return inventory;
  }

}
