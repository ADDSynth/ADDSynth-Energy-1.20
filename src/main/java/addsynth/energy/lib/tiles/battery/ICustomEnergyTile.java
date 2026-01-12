package addsynth.energy.lib.tiles.battery;

import addsynth.energy.lib.energy_network.CustomTransferData;
import addsynth.energy.lib.energy_network.EnergyTransferStage;
import addsynth.energy.lib.main.IEnergyUser;

/** This is used by {@link CustomTransferData} and any TileEntities that want to
 *  control what energy values to provide based on the {@link EnergyTransferStage}. */
public interface ICustomEnergyTile extends IEnergyUser {

  public double getAvailableEnergy(EnergyTransferStage stage);
  public double getRequestedEnergy(EnergyTransferStage stage);

}
