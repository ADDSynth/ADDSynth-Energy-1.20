package addsynth.energy.gameplay.machines.universal_energy_interface;

import addsynth.core.gui.section.FixedSizeGuiSection;
import addsynth.core.gui.widgets.buttons.Checkbox;
import addsynth.energy.ADDSynthEnergy;
import addsynth.energy.gameplay.NetworkHandler;
import addsynth.energy.gameplay.reference.GuiReference;
import addsynth.energy.lib.gui.GuiEnergyBase;
import addsynth.energy.lib.gui.widgets.EnergyProgressBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public final class GuiUniversalEnergyInterface extends GuiEnergyBase<TileUniversalEnergyInterface, ContainerUniversalEnergyInterface> {

  private static final int guiWidth = 345;
  private static final int guiHeight = 159;
  private static final int button_width = 100;
  private static final int button_height = 18;
  private static final int button_space = 4;
  private static final int total_button_height = (button_height * 6) + (button_space * 5);
  private static final int checkbox_space = 8;
  private static final int line_space = 20;
  private static final int arrow_space = 32;
  private static final int total_checkbox_height = 11 + (Checkbox.size*4) + (checkbox_space*2) + line_space;
  // 6 | button_width (100) | 6 | left_section (90) | arrow_width (32) | 6 | right_section (99) | 6 = 384
  // title (17) | 2 | checkbox_title (11) | checkbox_section (100) | 8 | bottom_section (15) | 6 = 159
  private static final FixedSizeGuiSection   button_section = new FixedSizeGuiSection(6, 17, button_width, total_button_height);
  private static final FixedSizeGuiSection external_section = new FixedSizeGuiSection(button_section.right + 6, 19, 90, total_checkbox_height);
  private static final FixedSizeGuiSection internal_section = new FixedSizeGuiSection(external_section.right + arrow_space + 6, 19, 99, total_checkbox_height);
  private static final int bottom_width = external_section.width + arrow_space + 6 + internal_section.width;
  private static final FixedSizeGuiSection   bottom_section = new FixedSizeGuiSection(button_section.right + 6, external_section.bottom + 8, bottom_width, 15);
  private final EnergyProgressBar energy_bar = new EnergyProgressBar(273, 123, 64, 11, 273, 168);
  
  private static final Component bi_directional   = Component.translatable("gui.addsynth_energy.transfer_mode.bi_directional");
  private static final Component external_battery = Component.translatable("gui.addsynth_energy.transfer_mode.external");
  private static final Component internal_battery = Component.translatable("gui.addsynth_energy.transfer_mode.internal");
  private static final Component transfer_out     = Component.translatable("gui.addsynth_energy.transfer_mode.transfer_out");
  private static final Component transfer_in      = Component.translatable("gui.addsynth_energy.transfer_mode.transfer_in");
  private static final Component no_transfer      = Component.translatable("gui.addsynth_energy.transfer_mode.no_transfer");
  private static final Component external_name    = Component.translatable("gui.addsynth_energy.universal_energy_interface.external_name");
  private static final Component internal_name    = Component.literal(ADDSynthEnergy.MOD_NAME);
  private static final Component checkbox_extract = Component.translatable("gui.addsynth_energy.universal_energy_interface.extract");
  private static final Component active_push      = Component.translatable("gui.addsynth_energy.universal_energy_interface.active_push");
  private static final Component checkbox_receive = Component.translatable("gui.addsynth_energy.universal_energy_interface.receive");
  private static final Component active_pull      = Component.translatable("gui.addsynth_energy.universal_energy_interface.active_pull");
  private static final Component is_free_energy   = Component.translatable("gui.addsynth_energy.universal_energy_interface.is_free_energy");
  private static final Component transfer_limit   = Component.translatable("gui.addsynth_energy.universal_energy_interface.transfer_limit").append(":");
  private static final Component transfer_rate    = Component.translatable("gui.addsynth_energy.universal_energy_interface.transfer_rate").append(":");
  
  public GuiUniversalEnergyInterface(final ContainerUniversalEnergyInterface container, final Inventory player_inventory, final Component title){
    super(guiWidth, guiHeight, container, player_inventory, title, GuiReference.universal_interface);
  }

  private final void addButton(final int x, final int y, final Component component, final int index){
    addRenderableWidget(Button.builder(component, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new SetTransferSettings(tile, index));
    }).bounds(x, y + (button_height*index) + (button_space*index), button_width, button_height).build());
  }

  private final void addCheckbox(final int x, final int y, final Component component, final int index){
    addRenderableWidget(new Checkbox(x, y, component){
      @Override
      public final boolean get_toggle_state(){
        return tile.getToggle(index);
      }
      @Override
      public final void onPress(){
        NetworkHandler.INSTANCE.sendToServer(new ToggleTransferSetting(tile, index));
      }
    });
  }

  @Override
  protected final void init(){
    super.init();
    final int button_x = leftPos + button_section.x;
    final int button_y = topPos + button_section.y;
    addButton(button_x, button_y, bi_directional, 0);
    addButton(button_x, button_y, external_battery, 1);
    addButton(button_x, button_y, internal_battery, 2);
    addButton(button_x, button_y, transfer_out, 3);
    addButton(button_x, button_y, transfer_in, 4);
    addButton(button_x, button_y, no_transfer, 5);
    int checkbox_x = leftPos + external_section.x;
    final int checkbox_y = topPos + external_section.top + 11;
    addCheckbox(checkbox_x, checkbox_y, checkbox_extract, 0);
    addCheckbox(checkbox_x, checkbox_y + Checkbox.size + checkbox_space, active_push, 1);
    addCheckbox(checkbox_x, checkbox_y + line_space + (Checkbox.size*2) + checkbox_space, checkbox_receive, 2);
    addCheckbox(checkbox_x, checkbox_y + line_space + (Checkbox.size*3) + (checkbox_space*2), active_pull, 3);
    checkbox_x = leftPos + internal_section.x;
    addCheckbox(checkbox_x, checkbox_y, checkbox_receive, 4);
    addCheckbox(checkbox_x, checkbox_y + line_space + (Checkbox.size*2) + checkbox_space, checkbox_extract, 5);
    addCheckbox(checkbox_x, checkbox_y + line_space + (Checkbox.size*3) + (checkbox_space*2), is_free_energy, 6);
  }

  @Override
  protected final void containerTick(){
  }

  @Override
  protected final void renderBg(GuiGraphics graphics, final float partialTicks, final int mouseX, final int mouseY){
    draw_custom_background_texture(graphics, 384, 192);
    energy_bar.drawVertical(graphics, this, energy);
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, final int mouseX, final int mouseY){
    draw_title(graphics);
    draw_text_center(graphics, external_name, external_section.center_x, external_section.top);
    draw_text_center(graphics, internal_name, internal_section.center_x, internal_section.top);
    draw_energy(graphics, bottom_section.left, bottom_section.center_x + 40, bottom_section.top + 2);
    draw_text_left(graphics, transfer_rate, bottom_section.left, bottom_section.top + 16);
    draw_text_right(graphics, String.format("%.2f", energy.getDifference()), bottom_section.center_x + 20, bottom_section.top + 16);
  }

}
