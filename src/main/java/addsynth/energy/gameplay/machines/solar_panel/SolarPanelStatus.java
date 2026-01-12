package addsynth.energy.gameplay.machines.solar_panel;

import addsynth.core.util.color.ColorCode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum SolarPanelStatus {

  NOT_CONNECTED         (ColorCode.ERROR,   "gui.addsynth_energy.solar_panel.status.not_connected"),
  DIMENSION_HAS_NO_LIGHT(ColorCode.ERROR,   "gui.addsynth_energy.solar_panel.status.dimension_has_no_light"),
  BLOCKED               (ColorCode.WARNING, "gui.addsynth_energy.solar_panel.status.blocked"),
  THUNDERING            (ColorCode.WARNING, "gui.addsynth_energy.solar_panel.status.thundering"),
  DARKNESS              (ColorCode.ERROR,   "gui.addsynth_energy.solar_panel.status.darkness"),
  WORKING               (ColorCode.GOOD,    "gui.addsynth_energy.solar_panel.status.working");

  private final MutableComponent message;
  private final ChatFormatting style;

  private SolarPanelStatus(final ChatFormatting formatting, final String translation_key){
    this.message = Component.translatable(translation_key);
    this.style = formatting != null ? formatting : ChatFormatting.RESET;
  }

  public final Component getMessage(){
    return message.withStyle(style);
  }

}
