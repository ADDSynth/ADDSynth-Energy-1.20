package addsynth.energy.lib.tiles.machines;

import addsynth.core.util.color.ColorCode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum MachineState {

  OFF         ("gui.addsynth_energy.machine_state.off"),
  POWERING_ON ("gui.addsynth_energy.machine_state.powering_on"),
  POWERING_OFF("gui.addsynth_energy.machine_state.powering_off"),
  IDLE        ("gui.addsynth_energy.machine_state.idle"),
  RUNNING     ("gui.addsynth_energy.machine_state.running");

  public static final MachineState[] value = MachineState.values();
  private final String translation_key;
  private final MutableComponent state;

  private MachineState(final String translation_key){
    this.translation_key = translation_key;
    this.state = Component.translatable(translation_key);
  }

  public final MutableComponent get(){
    return this != IDLE ? state : state.withStyle(ColorCode.GOOD);
  }

  public final MutableComponent get(String power_percentage){
    return Component.translatable(translation_key).append(" ").append(power_percentage);
  }

}
