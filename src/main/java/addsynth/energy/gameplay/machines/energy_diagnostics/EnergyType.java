package addsynth.energy.gameplay.machines.energy_diagnostics;

import javax.annotation.Nullable;
import addsynth.energy.gameplay.machines.universal_energy_interface.TileUniversalEnergyInterface;
import addsynth.energy.lib.main.IBattery;
import addsynth.energy.lib.main.IEnergyConsumer;
import addsynth.energy.lib.main.IEnergyGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

/** This is only used by {@link TileEnergyDiagnostics} to categorize
 *  the type of machines on the Energy Network. */
public enum EnergyType {

  GENERATOR(0, "gui.addsynth_energy.machine_type.generator"),
   RECEIVER(2, "gui.addsynth_energy.machine_type.receiver"),
    BATTERY(1, "gui.addsynth_energy.machine_type.battery"),
     CUSTOM(3, "gui.addsynth_energy.machine_type.custom");

  public final int order;
  public final Component component;

  private EnergyType(int compare_order, String translation_key){
   this.order = compare_order;
   component = Component.translatable(translation_key);
  }

  @Nullable
  public static final EnergyType determine(final BlockEntity tile){
    if(tile instanceof TileUniversalEnergyInterface){ return CUSTOM;    } // specifically check for this first
    if(tile instanceof IBattery                    ){ return BATTERY;   } // batteries can be IReceiver AND IConsumer
    if(tile instanceof IEnergyConsumer             ){ return RECEIVER;  } // Receivers are most common, check them next
    if(tile instanceof IEnergyGenerator            ){ return GENERATOR; }
    return null;
  }

}
