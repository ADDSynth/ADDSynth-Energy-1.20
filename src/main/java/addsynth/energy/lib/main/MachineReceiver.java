package addsynth.energy.lib.main;

import addsynth.energy.lib.config.MachineData;

/** The MachineReceiver is an extension of the Receiver which just checks the
 *  {@link MachineData} every tick to see if the energy values changed, like if
 *  any config values have changed, and updates the Energy object automatically.
 */
public class MachineReceiver extends Receiver {

  protected final MachineData data;

  public MachineReceiver(MachineData data){
    super(data.get_total_energy_needed(), data.get_max_receive());
    this.data = data;
  }

  @Override
  public boolean tick(){
    if(capacity.get() != data.get_total_energy_needed()){
      capacity.set(data.get_total_energy_needed());
      changed = true;
    }
    if(maxReceive.get() != data.get_max_receive()){
      maxReceive.set(data.get_max_receive());
      changed = true;
    }
    // reset energy I/O
    if(energy_in.get() > 0){
      energy_in.set(0);
      changed = true;
    }
    if(changed){
      changed = false;
      return true;
    }
    return false;
  }

}
