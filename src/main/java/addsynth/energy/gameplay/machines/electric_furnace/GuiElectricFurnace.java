package addsynth.energy.gameplay.machines.electric_furnace;

import addsynth.core.gui.widgets.rect.ProgressBar;
import addsynth.energy.gameplay.reference.GuiReference;
import addsynth.energy.lib.gui.GuiEnergyBase;
import addsynth.energy.lib.gui.widgets.WorkProgressBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class GuiElectricFurnace extends GuiEnergyBase<TileElectricFurnace, ContainerElectricFurnace> {

  private final WorkProgressBar work_progress_bar = new WorkProgressBar(80, 60, 14, 14, 200, 2);
  
  public GuiElectricFurnace(final ContainerElectricFurnace container, final Inventory player_inventory, final Component title){
    super(176, 172, container, player_inventory, title, GuiReference.electric_furnace);
  }

  @Override
  protected final void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_background_texture(graphics);
    work_progress_bar.draw(graphics, this, ProgressBar.Direction.BOTTOM_TO_TOP, tile);
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, int mouseX, int mouseY){
    draw_title(graphics);
    draw_energy_usage(graphics);
    draw_status(graphics, tile);
    graphics.renderItem(tile.getWorkingInventory().getStackInSlot(0), 80, 40);
    draw_text_center(graphics, work_progress_bar.getWorkTimeProgress(), center_x + 21, 65);
    draw_time_left_center(graphics, tile, 78);
  }

}
