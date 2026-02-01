package addsynth.energy.gameplay;

import addsynth.core.util.network.ADDSynthNetworkHandler;
import addsynth.energy.ADDSynthEnergy;
import addsynth.energy.gameplay.machines.circuit_fabricator.ChangeCircuitFabricatorRecipe;
import addsynth.energy.gameplay.machines.circuit_fabricator.CircuitFabricatorButtonMessage;
import addsynth.energy.gameplay.machines.energy_diagnostics.EnergyDiagnosticsMessage;
import addsynth.energy.gameplay.machines.solar_panel.SolarPanelData;
import addsynth.energy.gameplay.machines.universal_energy_interface.SetTransferSettings;
import addsynth.energy.gameplay.machines.universal_energy_interface.ToggleTransferSetting;
import addsynth.energy.lib.network_messages.*;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler extends ADDSynthNetworkHandler {

  public static final SimpleChannel INSTANCE = createChannel(ADDSynthEnergy.getLocation("network_channel"));

  public static final void registerMessages(){
    registerServerMessage( 0, INSTANCE, SwitchMachineMessage.class,          SwitchMachineMessage::decode);
    registerServerMessage( 1, INSTANCE, SetTransferSettings.class,           SetTransferSettings::new);
    registerServerMessage( 2, INSTANCE, ToggleAutoShutoffMessage.class,      ToggleAutoShutoffMessage::decode);
    registerServerMessage( 3, INSTANCE, ChangeCircuitFabricatorRecipe.class, ChangeCircuitFabricatorRecipe::new);
    registerClientMessage( 4, INSTANCE, EnergyDiagnosticsMessage.class,      EnergyDiagnosticsMessage::new);
    registerClientMessage( 5, INSTANCE, SolarPanelData.class,                SolarPanelData::new);
    registerServerMessage( 6, INSTANCE, ToggleTransferSetting.class,         ToggleTransferSetting::new);
    registerClientMessage( 7, INSTANCE, UpdateClientMachineStatusMessage.class, UpdateClientMachineStatusMessage::new);
    registerServerMessage( 8, INSTANCE, CircuitFabricatorButtonMessage.class, CircuitFabricatorButtonMessage::new);
  }

}
