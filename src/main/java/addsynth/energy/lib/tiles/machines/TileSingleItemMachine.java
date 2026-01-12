package addsynth.energy.lib.tiles.machines;

import java.util.function.Predicate;
import addsynth.core.game.inventory.IInputInventory;
import addsynth.core.game.inventory.IOutputInventory;
import addsynth.core.game.inventory.InputInventory;
import addsynth.core.game.inventory.InventoryUtil;
import addsynth.core.game.inventory.OutputInventory;
import addsynth.core.game.inventory.SlotData;
import addsynth.energy.lib.main.Receiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This machine has an Input Inventory and an Output Inventory.
 *  Since this machine 'works' on the items in the Input Inventory, and we allow the player
 *  to take out the items at any time, the 'work' done on the item must stay with the item,
 *  and there can only be 1 Input item at a time.
 */
public abstract class TileSingleItemMachine extends TileAbstractWorkMachine implements IInputInventory, IOutputInventory {

  protected final InputInventory input_inventory;
  protected final OutputInventory output_inventory;

  public TileSingleItemMachine(BlockEntityType type, BlockPos position, BlockState blockstate, Receiver energy){
    super(type, position, blockstate, MachineState.IDLE, energy);
    input_inventory = InputInventory.create(this, 1, 1);
    output_inventory = OutputInventory.create(this, 1);
  }

  public TileSingleItemMachine(BlockEntityType type, BlockPos position, BlockState blockstate,
                               Predicate<ItemStack> filter, Receiver energy){
    super(type, position, blockstate, MachineState.IDLE, energy);
    input_inventory = InputInventory.create(this, new SlotData(filter, 1));
    output_inventory = OutputInventory.create(this, 1);
  }

  @Override
  public void derivedTick(ServerLevel world, BlockState blockstate){
    machine_tick();
  }

  @Override
  protected final void machine_tick(){
    if(state == MachineState.IDLE){
      if(canDoWork()){
        state = MachineState.RUNNING;
        changed = true;
      }
    }
    if(state == MachineState.RUNNING){
      status = energy.isReceiving() ? MachineStatus.GOOD : MachineStatus.NOT_RECEIVING_ENERGY;
      doWork();
      if(canFinishWork()){
        finishWork();
      }
      if(!canDoWork()){ // check every tick if there's an item in the Input.
        state = MachineState.IDLE;
        changed = true;
      }
    }
  }

  /** Default behaviour is to check if the Input Inventory has an item in it. */
  protected boolean canDoWork(){
    return !input_inventory.isEmpty();
  }

  /** This is work that is done over time, to coincide with the energy level. */
  protected void doWork(){
  }

  /** Default behaviour is to first check if Energy is full, AND there's room in the output inventory. */
  protected boolean canFinishWork(){
    if(energy.isFull()){
      if(output_inventory.isEmpty()){
        status = MachineStatus.GOOD;
        return true;
      }
      status = MachineStatus.OUTPUT_FULL;
    }
    return false; 
  }

  /** Default behaviour is to empty energy and transfer 1 item from the Input to the Output. */
  protected void finishWork(){
    energy.setEmpty();
    final ItemStack stack = input_inventory.extractItemStack(0);
    output_inventory.add(0, stack);
  }

  protected final ItemStack getItemStack(){
    return input_inventory.getStackInSlot(0);
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
     input_inventory.load(nbt);
    output_inventory.load(nbt);
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    input_inventory.save(nbt);
    output_inventory.save(nbt);
   }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction side){
    if(remove == false){
      if(capability == ForgeCapabilities.ITEM_HANDLER){
        return InventoryUtil.getInventoryCapability(input_inventory, output_inventory, side);
      }
      return super.getCapability(capability, side);
    }
    return LazyOptional.empty();
  }

  @Override
  public double getRequestedEnergy(){
    if(state == MachineState.RUNNING){
      return energy.getRequestedEnergy();
    }
    return 0;
  }

  @Override
  public void onInventoryChanged(){
    changed = true;
  }

  @Override
  public final void drop_inventory(){
    InventoryUtil.drop_inventories(worldPosition, level, input_inventory, output_inventory);
  }

  @Override
  public final InputInventory getInputInventory(){
    return input_inventory;
  }

  @Override
  public final OutputInventory getOutputInventory(){
    return output_inventory;
  }

}
