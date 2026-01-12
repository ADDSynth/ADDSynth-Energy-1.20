package addsynth.energy.lib.gui;

import addsynth.core.container.TileEntityContainer;
import addsynth.core.gui.GuiContainerBase;
import addsynth.core.util.time.MinecraftTime;
import addsynth.energy.gameplay.reference.EnergyText;
import addsynth.energy.lib.main.Energy;
import addsynth.energy.lib.main.IEnergyUser;
import addsynth.energy.lib.tiles.machines.TileAbstractMachine;
import addsynth.energy.lib.tiles.machines.TileAbstractWorkMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Base Gui class for machines that use {@link Energy}. Contains helper
 *  functions for drawing energy variables and machine status.
 * @author ADDSynth
 * @param <T> BlockEntity that implements the {@link IEnergyUser} interface
 * @param <C> The Container class object for the machine
 */
public abstract class GuiEnergyBase<T extends BlockEntity & IEnergyUser, C extends TileEntityContainer<T>> extends GuiContainerBase<C> {

  protected final T tile;
  protected final Energy energy;

  public GuiEnergyBase(final C container, final Inventory player_inventory, final Component title, final ResourceLocation gui_texture_location){
    super(container, player_inventory, title, gui_texture_location);
    this.tile = (T)container.getTileEntity();
    this.energy = tile.getEnergy();
  }

  public GuiEnergyBase(int width, int height, C container, Inventory player_inventory, Component title, ResourceLocation gui_texture_location){
    super(width, height, container, player_inventory, title, gui_texture_location);
    this.tile = (T)container.getTileEntity();
    this.energy = tile.getEnergy();
  }

  /** Draws Energy: Level / Capacity in the standard location, just below the title, at y = 17 pixels. */
  protected final void draw_energy(final GuiGraphics graphics){
    this.draw_energy(graphics, 6, 17);
  }

  /** Draws energy with the header X position to the right of a power switch. */
  protected final void draw_energy_after_switch(final GuiGraphics graphics){
    this.draw_energy(graphics, 50, 21);
  }

  /** Draws energy with the header Y position below a power switch. */
  protected final void draw_energy_below_switch(final GuiGraphics graphics){
    this.draw_energy(graphics, 6, 37);
  }

  /** Draws energy with the header at the coordinates you specify, but the actual
   *  energy values are drawn right-aligned along the right-edge of the gui.
   * @param graphics
   * @param draw_x
   * @param draw_y
   */
  protected final void draw_energy(final GuiGraphics graphics, final int draw_x, final int draw_y){
    draw_energy(graphics, draw_x, right_edge, draw_y);
  }

  /** Draws the energy line. This specifically asks where to draw the header and the energy values.
   * @param graphics
   * @param draw_header_x
   * @param draw_energy_x
   * @param draw_y
   */
  protected final void draw_energy(final GuiGraphics graphics, final int draw_header_x, final int draw_energy_x, final int draw_y){
    if(energy != null){
      draw_text_left(graphics, EnergyText.energy_text, draw_header_x, draw_y);
      draw_text_right(graphics, energy.print(), draw_energy_x, draw_y);
    }
    else{
      draw_text_center(graphics, EnergyText.null_energy_reference, (draw_header_x + draw_energy_x) / 2, draw_y);
    }
  }

  /** Draws Energy Extraction for Generators. */
  protected final void draw_energy_extraction(final GuiGraphics graphics, final int draw_y){
    draw_text_left(graphics, EnergyText.extraction_text.getString()+":", 6, draw_y);
    draw_text_right(graphics, String.format("%.2f", energy.get_energy_out())+" / "+energy.getMaxExtract(), draw_y);
  }

  /** Draws the energy usage after the title. */
  protected final void draw_energy_usage(final GuiGraphics graphics){
    this.draw_energy_usage(graphics, 6, 17);
  }
  
  /** Draws the Energy Usage to the right of the Power Switch. */
  protected final void draw_energy_usage_after_switch(final GuiGraphics graphics){
    this.draw_energy_usage(graphics, 50, 21);
  }
  
  /** Draws the Energy Usage below the Power Switch. */
  protected final void draw_energy_usage_below_switch(final GuiGraphics graphics){
    this.draw_energy_usage(graphics, 6, 38);
  }
  
  protected final void draw_energy_usage(GuiGraphics graphics, final int draw_x, final int draw_y){
    if(energy != null){
      draw_text_left(graphics, EnergyText.efficiency_text.getString()+":", draw_x, draw_y);
      draw_text_right(graphics, energy.printEnergyUse(), draw_y);
    }
    else{
      draw_text_left(graphics, EnergyText.null_energy_reference, draw_x, draw_y);
    }
  }

  /** Draws the status at the default location, below the energy capacity line. */
  protected final void draw_status(GuiGraphics graphics, final TileAbstractWorkMachine machine){
    draw_text_left(graphics, EnergyText.status_text.get().append(machine.getStatus()), 6, 28);
  }

  /** Draws the machine's status at the Y level you specify. */
  protected final void draw_status(GuiGraphics graphics, final TileAbstractWorkMachine machine, final int y){
    draw_text_left(graphics, EnergyText.status_text.get().append(machine.getStatus()), 6, y);
  }

  /** Draws the machine's status at the coordinates you specify. */
  protected final void draw_status(GuiGraphics graphics, final TileAbstractWorkMachine machine, final int x, final int y){
    draw_text_left(graphics, EnergyText.status_text.get().append(machine.getStatus()), x, y);
  }

  /** Draws the status to the right of a power switch. */
  protected final void draw_status_after_switch(GuiGraphics graphics, final TileAbstractWorkMachine machine){
    draw_text_left(graphics, EnergyText.status_text.get().append(machine.getStatus()), 50, 21);
  }

  /** Draws the status below the power switch. */
  protected final void draw_status_below_switch(GuiGraphics graphics, final TileAbstractWorkMachine machine){
    draw_text_left(graphics, EnergyText.status_text.get().append(machine.getStatus()), 6, 37);
  }

  /** Draws machine time left at the bottom-left corner of the gui. */
  protected void draw_time_left(GuiGraphics graphics, final int draw_y, final TileAbstractMachine machine){
    draw_text_left(graphics, EnergyText.time_left_text.getString()+": "+MinecraftTime.print(machine.getTimeLeft()), 6, draw_y);
  }

  /** Draws machine time left at the bottom-center of the gui. */
  protected void draw_time_left_center(GuiGraphics graphics, final int draw_y, final TileAbstractMachine machine){
    draw_text_center(graphics, EnergyText.time_left_text.getString()+": "+MinecraftTime.print(machine.getTimeLeft()), imageWidth/2, draw_y);
  }

  /** Draws charge time at bottom-left of gui. */
  protected final void draw_energy_difference(GuiGraphics graphics, final int draw_y){
    if(energy == null){
      draw_text_left(graphics, EnergyText.null_energy_reference, 6, draw_y);
      return;
    }
    final double difference = energy.getDifference();
    switch((int)Math.signum(difference)){
    case 1:
      draw_text_left(graphics, EnergyText.full_charge_time_text.getString()+": "+MinecraftTime.print((int)Math.ceil(energy.getEnergyNeeded() / difference)), 6, draw_y);
      break;
    case -1:
      draw_text_left(graphics, EnergyText.charge_remaining_text.getString()+": "+MinecraftTime.print((int)Math.ceil(energy.getEnergy() / (-difference))), 6, draw_y);
      break;
    case 0:
      draw_text_left(graphics, EnergyText.no_energy_change_text, 6, draw_y);
      break;
    }
  }

  /** Draws charge time at bottom-center of gui. */
  protected final void draw_energy_difference_center(GuiGraphics graphics, final int draw_y){
    final int draw_x = imageWidth/2;
    if(energy == null){
      draw_text_center(graphics, EnergyText.null_energy_reference, draw_x, draw_y);
      return;
    }
    final double difference = energy.getDifference();
    switch((int)Math.signum(difference)){
    case 1:
      draw_text_center(graphics, EnergyText.full_charge_time_text.getString()+": "+MinecraftTime.print((int)Math.ceil(energy.getEnergyNeeded() / difference)), draw_x, draw_y);
      break;
    case -1:
      draw_text_center(graphics, EnergyText.charge_remaining_text.getString()+": "+MinecraftTime.print((int)Math.ceil(energy.getEnergy() / (-difference))), draw_x, draw_y);
      break;
    case 0:
      draw_text_center(graphics, EnergyText.no_energy_change_text, draw_x, draw_y);
      break;
    }
  }

}
