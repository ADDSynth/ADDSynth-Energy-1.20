package addsynth.energy.gameplay.machines.generator;

import addsynth.energy.gameplay.reference.GuiReference;
import addsynth.energy.gameplay.reference.EnergyText;
import addsynth.energy.lib.gui.GuiEnergyBase;
import addsynth.energy.lib.gui.widgets.EnergyProgressBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class GuiGenerator extends GuiEnergyBase<TileGenerator, ContainerGenerator> {

  private final EnergyProgressBar energy_progress_bar = new EnergyProgressBar(8, 80, 168, 20, 8, 194);

  private static final int input_text_y = 24;
  private static final int line_1 = 44;
  private static final int line_2 = 56;
  private static final int line_3 = 68;

  public GuiGenerator(ContainerGenerator container, Inventory player_inventory, Component title){
    super(184, 188, container, player_inventory, title, GuiReference.generator);
  }

  @Override
  protected final void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_background_texture(graphics);
    energy_progress_bar.drawHorizontal(graphics, this, energy);
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, final int mouseX, final int mouseY){
    draw_title(graphics);
    draw_text_right(graphics, EnergyText.input_text.getString()+":", 79, input_text_y);
    
    draw_energy(graphics, 6, line_1);
    //draw_energy_extraction(matrix, line_2);
    draw_text_center(graphics, EnergyText.max_extract_text.getString()+": "+energy.getMaxExtract(), center_x, line_2);
    draw_text_center(graphics, energy_progress_bar.getEnergyPercentage(),    center_x, line_3);
    draw_energy_difference(graphics, 94);
  }
}
