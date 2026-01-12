package addsynth.energy.gameplay.machines.compressor;

import addsynth.energy.gameplay.reference.GuiReference;
import addsynth.energy.lib.gui.GuiEnergyBase;
import addsynth.energy.lib.gui.widgets.WorkProgressBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class GuiCompressor extends GuiEnergyBase<TileCompressor, ContainerCompressor> {

  private final WorkProgressBar work_progress_bar = new WorkProgressBar(8, 79, 160, 5, 8, 194);
  
  public GuiCompressor(final ContainerCompressor container, final Inventory player_inventory, final Component title){
    super(176, 182, container, player_inventory, title, GuiReference.compressor);
  }

  @Override
  protected final void renderBg(GuiGraphics graphics, final float partialTicks, final int mouseX, final int mouseY){
    draw_background_texture(graphics);
    work_progress_bar.draw(graphics, this, tile);
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, final int mouseX, final int mouseY){
    draw_title(graphics);
    draw_energy_usage(graphics);
    draw_status(graphics, tile);
    graphics.renderItem(tile.getWorkingInventory().getStackInSlot(0), 80, 42);
    draw_text_center(graphics, work_progress_bar.getWorkTimeProgress(), 67);
    draw_time_left_center(graphics, 88, tile);
  }

}
