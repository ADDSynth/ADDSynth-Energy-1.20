package addsynth.energy.gameplay;

import addsynth.energy.ADDSynthEnergy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class CreativeTab {

  private static final ResourceLocation id = ADDSynthEnergy.getLocation("creative_tab");
  public  static final ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);

  public static final void register(final Registry<CreativeModeTab> registry){
    final CreativeModeTab creative_tab = CreativeModeTab.builder()
      .title(Component.literal(ADDSynthEnergy.MOD_NAME))
      .icon(() -> new ItemStack(Item.BY_BLOCK.get(EnergyBlocks.wire.get())))
      .displayItems((displayParameters, output) -> {
        output.accept(EnergyItems.low_voltage_wire.get());
        output.accept(EnergyBlocks.wire.get());
        output.accept(EnergyBlocks.generator.get());
        output.accept(EnergyBlocks.charger.get());
        output.accept(EnergyBlocks.energy_storage.get());
        output.accept(EnergyBlocks.compressor.get());
        output.accept(EnergyBlocks.electric_furnace.get());
        output.accept(EnergyBlocks.circuit_fabricator.get());
        output.accept(EnergyBlocks.universal_energy_machine.get());
        output.accept(EnergyBlocks.energy_diagnostics_block.get());
        output.accept(EnergyItems.photovoltaic_cell.get());
        output.accept(EnergyBlocks.solar_panel.get());
        output.accept(EnergyBlocks.solar_panel_controller.get());
        output.accept(EnergyItems.battery.get());
        output.accept(EnergyItems.power_core.get());
        output.accept(EnergyItems.advanced_power_core.get());
        output.accept(EnergyItems.power_regulator.get());
        output.accept(EnergyItems.steel_rod.get());
        output.accept(EnergyItems.energy_tool_part.get());
        output.accept(EnergyItems.circuit_tier_1.get());
        output.accept(EnergyItems.circuit_tier_2.get());
        output.accept(EnergyItems.circuit_tier_3.get());
        output.accept(EnergyItems.circuit_tier_4.get());
        output.accept(EnergyItems.circuit_tier_5.get());
        output.accept(EnergyItems.circuit_tier_6.get());
        output.accept(EnergyItems.circuit_tier_7.get());
        output.accept(EnergyItems.circuit_tier_8.get());
        output.accept(EnergyItems.circuit_tier_9.get());
        output.accept(EnergyItems.energy_shovel.get());
        output.accept(EnergyItems.energy_pickaxe.get());
        output.accept(EnergyItems.energy_axe.get());
        output.accept(EnergyItems.energy_hoe.get());
      })
      .withTabsBefore(addsynth.core.gameplay.CreativeTab.key,
                      addsynth.material.CreativeTab.key
      ).build();
    Registry.register(registry, key, creative_tab);
  }

}
