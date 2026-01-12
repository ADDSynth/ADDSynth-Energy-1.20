package addsynth.energy.lib.energy_network;

import addsynth.energy.gameplay.machines.universal_energy_interface.TileUniversalEnergyInterface;

/** This is mainly used in {@link CustomTransferData} and {@link TileUniversalEnergyInterface}
 *  to determine what energy values to provide at each transfer stage. */
public enum EnergyTransferStage {

  FREE_GENERATOR, GENERATOR, BATTERY, RECEIVER

}
