package addsynth.energy.gameplay.items.energy_tools;

import java.util.List;
import addsynth.core.game.item.tool.ToolConstants;
import addsynth.core.game.item.tool.ToolUtil;
import addsynth.energy.lib.items.energy.EnergyItemCapabilityProvider;
import addsynth.energy.lib.items.energy.ItemEnergy;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public final class EnergyAxe extends AxeItem {

  public EnergyAxe(){
    super(EnergyTools.TIER, ToolConstants.axe_damage, ToolConstants.axe_speed, new Item.Properties().setNoRepair());
  }

  @Override
  public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt){
    return EnergyItemCapabilityProvider.createChargeOnly(stack, EnergyTools.DEFAULT_ENERGY);
  }

  @Override
  public boolean mineBlock(ItemStack itemstack, Level level, BlockState blockstate, BlockPos position, LivingEntity entity){
    if(ToolUtil.mine(level, blockstate, position)){
      EnergyTools.reduceEnergy(itemstack);
    }
    return true;
  }

  @Override
  public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity attacker){
    if(ItemEnergy.hasEnergy(itemstack)){
      EnergyTools.attackEntity(itemstack);
    }
    return true;
  }

  @Override
  public InteractionResult useOn(UseOnContext useContext){
    final ItemStack stack = useContext.getItemInHand();
    return ItemEnergy.hasEnergy(stack) ? super.useOn(useContext) : InteractionResult.PASS;
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemstack){
    return ItemEnergy.hasEnergy(itemstack) ? super.getAttributeModifiers(slot, itemstack) : ImmutableMultimap.of();
  }

  @Override
  public float getDestroySpeed(ItemStack itemstack, BlockState blockstate){
    return ItemEnergy.hasEnergy(itemstack) ? super.getDestroySpeed(itemstack, blockstate) : 1.0f;
  }

  @Override
  public boolean isCorrectToolForDrops(ItemStack stack, BlockState state){
    return ItemEnergy.hasEnergy(stack) ? super.isCorrectToolForDrops(stack, state) : false;
  }

  @Override
  public boolean canDisableShield(ItemStack itemstack, ItemStack shield, LivingEntity entity, LivingEntity attacker){
    return ItemEnergy.hasEnergy(itemstack);
  }

  @Override
  public void appendHoverText(ItemStack itemstack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag){
    tooltipComponents.add(ItemEnergy.getEnergyComponent(itemstack));
  }

  @Override
  public boolean isBarVisible(ItemStack itemstack){
    return true;
  }

  @Override
  public int getBarColor(ItemStack itemstack){
    return EnergyTools.getColor(itemstack);
  }

  @Override
  public int getBarWidth(ItemStack itemstack){
    return ItemEnergy.getBarWidth(itemstack);
  }

  @Override
  public boolean isEnchantable(ItemStack itemstack){
    return false;
  }

  @Override
  public int getEnchantmentValue(){
    return 0;
  }

  @Override
  public int getEnchantmentValue(ItemStack stack){
    return 0;
  }

  @Override
  public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair){
    return false;
  }

}
