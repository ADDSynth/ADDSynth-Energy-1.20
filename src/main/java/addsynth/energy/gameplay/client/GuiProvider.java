package addsynth.energy.gameplay.client;

import addsynth.energy.gameplay.machines.energy_diagnostics.GuiEnergyDiagnostics;
import addsynth.energy.gameplay.machines.energy_diagnostics.TileEnergyDiagnostics;
import addsynth.energy.gameplay.machines.solar_panel.SolarPanelControllerGui;
import addsynth.energy.gameplay.machines.solar_panel.SolarPanelControllerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class GuiProvider {

  @SuppressWarnings("resource")
  public static final void openEnergyDiagnostics(final TileEnergyDiagnostics tile, final Component title){
    Minecraft.getInstance().setScreen(new GuiEnergyDiagnostics(tile, title));
  }

  @SuppressWarnings("resource")
  public static final void openSolarPanelController(final SolarPanelControllerTile tile, final Component title){
    Minecraft.getInstance().setScreen(new SolarPanelControllerGui(tile, title));
  }

}
