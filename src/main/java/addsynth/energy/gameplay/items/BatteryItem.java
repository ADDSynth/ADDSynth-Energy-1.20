package addsynth.energy.gameplay.items;

import java.util.List;
import addsynth.core.util.color.Colors;
import addsynth.energy.lib.items.energy.EnergyItemCapabilityProvider;
import addsynth.energy.lib.items.energy.ItemEnergy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public final class BatteryItem extends Item {

  private static final int battery_charge = 32_000;

  public BatteryItem(){
    super(new Item.Properties());
  }

  @Override
  @Nullable
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt){
    return EnergyItemCapabilityProvider.create(stack, battery_charge);
  }

  @Override
  public void appendHoverText(ItemStack itemstack, @javax.annotation.Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag){
    tooltipComponents.add(ItemEnergy.getEnergyComponent(itemstack));
  }

  @Override
  public boolean isBarVisible(ItemStack itemstack){
    return true;
  }

  @Override
  public int getBarColor(ItemStack itemstack){
    return Colors.YELLOW.value;
  }

  @Override
  public int getBarWidth(ItemStack itemstack){
    return ItemEnergy.getBarWidth(itemstack);
  }

}
