package addsynth.energy.gameplay.items.energy_tools;

import addsynth.energy.lib.items.energy.ItemEnergy;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

public final class EnergyTools {

  public static final int ENERGY_USAGE = 3;
  // Got stuck between wanting Energy Tools to use a lot of energy but also charge fast.
  public static final int DEFAULT_ENERGY = 1600 * ENERGY_USAGE;

  public static final Tier TIER = new Tier(){
    @Override
    public int getUses(){return 0;}
    @Override
    public float getSpeed(){return Tiers.IRON.getSpeed();}
    @Override
    public float getAttackDamageBonus(){return 2.5f;}
    @Override
    @Deprecated
    public int getLevel(){return Tiers.DIAMOND.getLevel();}
    @Override
    public int getEnchantmentValue(){return 0;}
    @Override
    public Ingredient getRepairIngredient(){return Ingredient.EMPTY;}
  };

  public static final int getColor(ItemStack stack){
    // Mix from #00FFFF to #FF99FF   https://www.w3schools.com/colors/colors_mixer.asp
    return Mth.hsvToRgb(0.5f, 1.0f, 1.0f);
  }

  public static final void reduceEnergy(ItemStack itemstack){
    ItemEnergy.useEnergy(itemstack, ENERGY_USAGE);
  }
  
  public static final void attackEntity(ItemStack itemstack){
    ItemEnergy.useEnergy(itemstack, ENERGY_USAGE * 2);
  }

}
