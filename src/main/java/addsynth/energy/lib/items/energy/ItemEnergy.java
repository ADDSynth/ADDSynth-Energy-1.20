package addsynth.energy.lib.items.energy;

import javax.annotation.Nullable;
import addsynth.energy.ADDSynthEnergy;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

/** This implements an {@link IEnergyStorage} capability around an ItemStack. */
public class ItemEnergy implements IEnergyStorage {

  private static final int DEFAULT_TRANSFER_RATE = 20;
  public static final String ENERGY_LABEL = "Energy";
  public static final String CAPACITY_LABEL = "Energy Capacity";

  private final ItemStack itemstack; // How does Forge invalidate the IEnergyStorage capability on the ItemStack if people can cache the IEnergyStorage?
  private final int maxTransferRate;

  public ItemEnergy(final ItemStack itemstack, int max_energy){
    initEnergy(itemstack, max_energy);
    this.itemstack = itemstack;
    this.maxTransferRate = DEFAULT_TRANSFER_RATE;
  }

  public ItemEnergy(final ItemStack itemstack, int max_energy, int maxTransferRate){
    initEnergy(itemstack, max_energy);
    this.itemstack = itemstack;
    this.maxTransferRate = maxTransferRate;
  }

  /** When syncing clients/server, vanilla loads ItemStack from NBT structure, which would already contain
   *  existing Energy tag. Only init Energy if it doesn't exist. */
  private static final void initEnergy(ItemStack itemstack, int max_energy){
    final CompoundTag tag = itemstack.getOrCreateTag();
    if(!tag.contains(ENERGY_LABEL)){
      tag.putInt(ENERGY_LABEL, max_energy);
    }
    if(!tag.contains(CAPACITY_LABEL)){
      tag.putInt(CAPACITY_LABEL, max_energy);
    }
    itemstack.setTag(tag);
  }

  // ============================================================================================

  public static final void registerItemProperty(){
    ItemProperties.registerGeneric(ADDSynthEnergy.getLocation("energy"), (ItemStack itemstack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) -> {
      return getEnergy(itemstack);
    });
    ItemProperties.registerGeneric(ADDSynthEnergy.getLocation("energy_percentage"), (ItemStack itemstack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) -> {
      return getEnergyPercentage(itemstack);
    });
  }

  public static final int getBarWidth(ItemStack stack){
    return Math.round(getEnergyPercentage(stack) * 13);
  }

  public static final Component getEnergyComponent(ItemStack stack){
    final CompoundTag tag = stack.getOrCreateTag();
    final int energy = tag.getInt(ItemEnergy.ENERGY_LABEL);
    final int capacity = tag.getInt(ItemEnergy.CAPACITY_LABEL);
    return Component.translatable("gui.addsynth_energy.tooltip.energy", energy, capacity);
  }

  public static final void useEnergy(ItemStack itemstack, int energy_used){
    final CompoundTag tag = itemstack.getOrCreateTag();
    final int energy = tag.getInt(ENERGY_LABEL);
    if(energy > 0){
      tag.putInt(ENERGY_LABEL, Math.max(energy - energy_used, 0));
      itemstack.setTag(tag);
    }
  }

  // ============================================================================================

  public static final int getEnergy(final ItemStack itemstack){
    final CompoundTag tag = itemstack.getTag();
    return tag != null ? tag.getInt(ENERGY_LABEL) : 0;
  }

  public static final void setEnergy(final ItemStack itemstack, final int energy){
    final CompoundTag tag = itemstack.getOrCreateTag();
    tag.putInt(ENERGY_LABEL, energy);
    itemstack.setTag(tag);
  }

  public static final int getCapacity(final ItemStack itemstack){
    final CompoundTag tag = itemstack.getTag();
    return tag != null ? tag.getInt(CAPACITY_LABEL) : 0;
  }

  public static final void setCapacity(final ItemStack itemstack, final int capacity){
    final CompoundTag tag = itemstack.getOrCreateTag();
    tag.putInt(CAPACITY_LABEL, capacity);
    itemstack.setTag(tag);
  }

  public static final void setEnergyAndCapacity(final ItemStack itemstack, final int max_energy){
    final CompoundTag tag = itemstack.getOrCreateTag();
    tag.putInt(ENERGY_LABEL, max_energy);
    tag.putInt(CAPACITY_LABEL, max_energy);
    itemstack.setTag(tag);
  }

  public static final boolean hasEnergy(ItemStack itemstack){
    return getEnergy(itemstack) > 0;
  }

  public static final float getEnergyPercentage(ItemStack itemstack){
    final CompoundTag tag = itemstack.getOrCreateTag();
    final float energy = tag.getInt(ENERGY_LABEL);
    final float capacity = tag.getInt(CAPACITY_LABEL);
    return capacity > 0 ? energy / capacity : 0;
  }

  // ============================================================================================

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate){
    if(canReceive()){
      final CompoundTag tag = itemstack.getOrCreateTag();
      final int energy = tag.getInt(ENERGY_LABEL);
      final int capacity = tag.getInt(CAPACITY_LABEL);
      final int energy_needed = capacity - energy;
      final int energy_received = Math.min(Math.min(energy_needed, maxReceive), maxTransferRate);
      if(!simulate){
        setEnergy(itemstack, energy + energy_received);
      }
      return energy_received;
    }
    return 0;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate){
    if(canExtract()){
      final int energy = getEnergy(itemstack);
      final int energy_extracted = Math.min(Math.min(energy, maxExtract), maxTransferRate);
      if(!simulate){
        setEnergy(itemstack, energy - energy_extracted);
      }
      return energy_extracted;
    }
    return 0;
  }

  @Override
  public int getEnergyStored(){
    return getEnergy(itemstack);
  }

  @Override
  public int getMaxEnergyStored(){
    return getCapacity(itemstack);
  }

  @Override
  public boolean canExtract(){
    return true;
  }

  @Override
  public boolean canReceive(){
    return true;
  }

}
