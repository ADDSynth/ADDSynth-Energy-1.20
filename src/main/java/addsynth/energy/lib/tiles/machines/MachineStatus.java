package addsynth.energy.lib.tiles.machines;

import addsynth.core.util.color.ColorCode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** Separate from {@link MachineState}, the machine may not be able to perform normal function
 *  based on some condition. Since Machine Status can be calculated on the server, it does not
 *  need to be saved and only needs to be sent to the client.
 */
public enum MachineStatus {

  GOOD                (false, "Normal"), // no need to specify a translation key as GOOD status will never be displayed.
  OUTPUT_FULL         (true,  "gui.addsynth_energy.machine_state.output_full"),
  // REMOVE: MachineStatus.NO_ENERGY, I checked, it was commented code in TileAlwaysOnMachine, and TileStandardWorkMachine,
  //         displays when machine had no energy. Has been superseded by NOT_RECEIVING_ENERGY. Probably will be removed.
  // NO_ENERGY           (true,  "gui.addsynth_energy.machine_state.no_energy"),
  NOT_RECEIVING_ENERGY(true,  "gui.addsynth_energy.machine_state.not_receiving_energy");

  private final MutableComponent status;

  private MachineStatus(boolean error, String translation_key){
    status = Component.translatable(translation_key);
  }

  public boolean isError(){
    return this != GOOD;
  }

  public MutableComponent get(){
    return this != GOOD ? status.withStyle(ColorCode.ERROR) : status;
  }

}
