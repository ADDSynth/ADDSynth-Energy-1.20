package addsynth.energy.registers;

import addsynth.core.game.registry.BlockEntityHolder;
import addsynth.energy.gameplay.EnergyBlocks;
import addsynth.energy.gameplay.machines.charger.TileCharger;
import addsynth.energy.gameplay.machines.circuit_fabricator.TileCircuitFabricator;
import addsynth.energy.gameplay.machines.compressor.TileCompressor;
import addsynth.energy.gameplay.machines.electric_furnace.TileElectricFurnace;
import addsynth.energy.gameplay.machines.energy_diagnostics.TileEnergyDiagnostics;
import addsynth.energy.gameplay.machines.energy_storage.TileEnergyStorage;
import addsynth.energy.gameplay.machines.energy_wire.TileEnergyWire;
import addsynth.energy.gameplay.machines.generator.TileGenerator;
import addsynth.energy.gameplay.machines.solar_panel.SolarPanelTile;
import addsynth.energy.gameplay.machines.solar_panel.SolarPanelControllerTile;
import addsynth.energy.gameplay.machines.universal_energy_interface.TileUniversalEnergyInterface;
import addsynth.energy.gameplay.reference.Names;

public final class Tiles {

  public static final BlockEntityHolder<TileEnergyWire> ENERGY_WIRE =
    new BlockEntityHolder<>(Names.ENERGY_WIRE, TileEnergyWire::new, EnergyBlocks.wire);

  public static final BlockEntityHolder<TileGenerator> GENERATOR =
    new BlockEntityHolder<>(Names.GENERATOR, TileGenerator::new, EnergyBlocks.generator);

  public static final BlockEntityHolder<TileCharger> CHARGER =
    new BlockEntityHolder<>(Names.CHARGER, TileCharger::new, EnergyBlocks.charger);

  public static final BlockEntityHolder<TileEnergyStorage> ENERGY_CONTAINER =
    new BlockEntityHolder<>(Names.ENERGY_STORAGE, TileEnergyStorage::new, EnergyBlocks.energy_storage);

  public static final BlockEntityHolder<TileCompressor> COMPRESSOR =
    new BlockEntityHolder<>(Names.COMPRESSOR, TileCompressor::new, EnergyBlocks.compressor);

  public static final BlockEntityHolder<TileElectricFurnace> ELECTRIC_FURNACE =
    new BlockEntityHolder<>(Names.ELECTRIC_FURNACE, TileElectricFurnace::new, EnergyBlocks.electric_furnace);

  public static final BlockEntityHolder<TileCircuitFabricator> CIRCUIT_FABRICATOR =
    new BlockEntityHolder<>(Names.CIRCUIT_FABRICATOR, TileCircuitFabricator::new, EnergyBlocks.circuit_fabricator);

  public static final BlockEntityHolder<TileUniversalEnergyInterface> UNIVERSAL_ENERGY_INTERFACE =
    new BlockEntityHolder<>(Names.UNIVERSAL_ENERGY_INTERFACE, TileUniversalEnergyInterface::new, EnergyBlocks.universal_energy_machine);

  public static final BlockEntityHolder<TileEnergyDiagnostics> ENERGY_DIAGNOSTICS_BLOCK =
    new BlockEntityHolder<>(Names.ENERGY_DIAGNOSTICS_BLOCK, TileEnergyDiagnostics::new, EnergyBlocks.energy_diagnostics_block);

  // public static final BlockEntityHolder<LowVoltageWireTile> LOW_VOLTAGE_WIRE =
  //   new BlockEntityHolder<>(Names.LOW_VOLTAGE_WIRE, LowVoltageWireTile::new, EnergyBlocks.low_voltage_wire);

  public static final BlockEntityHolder<SolarPanelTile> SOLAR_PANEL =
    new BlockEntityHolder<>(Names.SOLAR_PANEL, SolarPanelTile::new, EnergyBlocks.solar_panel);

  public static final BlockEntityHolder<SolarPanelControllerTile> SOLAR_PANEL_CONTROLLER =
    new BlockEntityHolder<>(Names.SOLAR_PANEL_CONTROLLER, SolarPanelControllerTile::new, EnergyBlocks.solar_panel_controller);

}
