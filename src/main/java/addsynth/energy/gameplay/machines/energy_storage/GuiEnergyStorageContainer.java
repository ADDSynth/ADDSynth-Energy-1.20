package addsynth.energy.gameplay.machines.energy_storage;

import addsynth.energy.gameplay.reference.GuiReference;
import addsynth.energy.gameplay.reference.EnergyText;
import addsynth.energy.lib.gui.GuiEnergyBase;
import addsynth.energy.lib.gui.widgets.EnergyProgressBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class GuiEnergyStorageContainer extends GuiEnergyBase<TileEnergyStorage, ContainerEnergyStorage> {

  private static final int draw_energy_level_y = 36;
  private final EnergyProgressBar energy_bar = new EnergyProgressBar(9, 59, 174, 17, 9, 106);

  public GuiEnergyStorageContainer(final ContainerEnergyStorage container, final Inventory player_inventory, final Component title){
    super(190, 94, container, player_inventory, title, GuiReference.energy_storage);
  }

  @Override
  protected final void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_background_texture(graphics);
    energy_bar.drawHorizontal(graphics, this, energy);
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, final int mouseX, final int mouseY){
    draw_title(graphics);
    draw_text_center(graphics, EnergyText.energy_stored_text.getString()+":", center_x, 25);
    draw_text_right(graphics, String.format("%.2f", energy.getEnergy()), 88, draw_energy_level_y);
    draw_text_left(graphics, "/ "+energy.getCapacity(), 93, draw_energy_level_y);
    draw_text_center(graphics, energy_bar.getEnergyPercentage(), center_x, 47);
    draw_energy_difference(graphics, 80);
  }

}
