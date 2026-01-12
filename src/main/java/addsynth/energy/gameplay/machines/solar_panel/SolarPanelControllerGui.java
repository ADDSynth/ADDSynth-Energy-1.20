package addsynth.energy.gameplay.machines.solar_panel;

import java.time.LocalTime;
import addsynth.core.gui.GuiBase;
import addsynth.core.gui.util.GuiUtil;
import addsynth.energy.gameplay.config.Config;
import addsynth.energy.gameplay.reference.GuiReference;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class SolarPanelControllerGui extends GuiBase {

  private static final Component     status_text = Component.translatable("gui.addsynth_energy.solar_panel.status");
  private static final Component     energy_text = Component.translatable("gui.addsynth_energy.solar_panel.energy");
  private static final Component      phase_text = Component.translatable("gui.addsynth_energy.solar_panel.phase");
  private static final Component      count_text = Component.translatable("gui.addsynth_energy.solar_panel.count");
  private static final Component    blocked_text = Component.translatable("gui.addsynth_energy.solar_panel.blocked_count");
  private static final Component efficiency_text = Component.translatable("gui.addsynth_energy.solar_panel.efficiency");

  private static final int guiWidth = 159;
  private static final int guiHeight = 89;
  private static final int[] lines = {17, 28, 39, 50, 61, 72};

  private final SolarPanelControllerTile tile;
  private int text_width;

  public SolarPanelControllerGui(SolarPanelControllerTile controller, Component title){
    super(guiWidth, guiHeight, title, GuiReference.solar_panel_controller);
    this.tile = controller;
  }

  @Override
  protected final void drawGuiBackgroundLayer(GuiGraphics graphics, float partialTicks, int mouse_x, int mouse_y){
    draw_custom_background_texture(graphics, guiWidth, guiHeight);
  }

  @Override
  protected final void drawGuiForegroundLayer(GuiGraphics graphics, int mouse_x, int mouse_y){
    draw_title(graphics);
    text_width = 6 + GuiUtil.getMaxStringWidth(font, status_text, energy_text, phase_text, count_text, blocked_text, efficiency_text);
    draw_text_right(graphics,     status_text, text_width, lines[0]);
    draw_text_right(graphics,     energy_text, text_width, lines[1]);
    draw_text_right(graphics,      phase_text, text_width, lines[2]);
    draw_text_right(graphics,      count_text, text_width, lines[3]);
    draw_text_right(graphics,    blocked_text, text_width, lines[4]);
    draw_text_right(graphics, efficiency_text, text_width, lines[5]);
    text_width += 5;
    draw_text_left(graphics, tile.getStatusMessage(), text_width, lines[0]);
    draw_text_left(graphics, String.format("%.2f", tile.getEnergyValue()), text_width, lines[1]);
    switch(Config.SOLAR_PANEL.phase_display.get()){
    case DEGREES:
      draw_text_left(graphics, String.format("%.1f", tile.getPhase() * 360)+'°', text_width, lines[2]);
      break;
    case PERCENTAGE:
      draw_text_left(graphics, String.format("%.1f", tile.getPhase() * 100)+'%', text_width, lines[2]);
      break;
    case TICKS:
      draw_text_left(graphics, Integer.toString(tile.getTicks()), text_width, lines[2]);
      break;
    case TIME:
      final int ticks = tile.getTicks();
      final int hours = ((ticks / 1000) + 6) % 24;
      final int minutes = (int)((ticks % 1000)*0.06);
      final LocalTime time = LocalTime.of(hours, minutes, 0);
      draw_text_left(graphics, String.format("%1$Tl:%1$TM %1$Tp", time), text_width, lines[2]);
      break;
    }
    draw_text_left(graphics, Integer.toString(tile.getSolarPanelCount()), text_width, lines[3]);
    draw_text_left(graphics, Integer.toString(tile.getBlockedCount()), text_width, lines[4]);
    draw_text_left(graphics, String.format("%.2f", tile.getEfficiency())+'%', text_width, lines[5]);
  }

}
