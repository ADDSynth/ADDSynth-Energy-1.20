package addsynth.energy.lib.config;

import javax.annotation.Nonnegative;
import addsynth.core.util.time.TimeConstants;

public class MachineData {

  public static final float DEFAULT_IDLE_ENERGY = 1.0f / TimeConstants.ticks_per_second;

  public final MachineType type;

  /** Number of work units needed to perform 1 operation. */
  @Nonnegative
  protected final int WORK_TIME;

  /** Number of Ticks the machine spends 'powering on' when the player turns the machine off. */
  @Nonnegative
  protected final int POWER_ON_TIME;

  /** Amount of Energy needed to increment the work time by 1. */
  @Nonnegative
  protected final double ENERGY_PER_TICK;

  /** Amount of Energy that is used every tick when the machine is not doing work. */
  @Nonnegative
  protected final double IDLE_ENERGY;

  /** Total amount energy needed to perform 1 operation. */
  public final double get_total_energy_needed(){
    // use functions to get because they are overriden in MachineDataConfig to get the values from the config file.
    return get_max_receive() * get_work_time();
  }

  public MachineData(MachineType type, int work_time, double energy_needed){
    this(type, work_time, energy_needed, DEFAULT_IDLE_ENERGY, 0);
  }

  public MachineData(MachineType type, int work_time, double energy_needed, double idle_energy){
    this(type, work_time, energy_needed, idle_energy, 0);
  }

  public MachineData(MachineType type, int work_time, double energy_needed, double idle_energy, int power_on_time){
    this.type            = type;
    this.WORK_TIME       = work_time;
    this.POWER_ON_TIME   = power_on_time;
    this.ENERGY_PER_TICK = energy_needed;
    this.IDLE_ENERGY     = idle_energy < 0 ? DEFAULT_IDLE_ENERGY : idle_energy;
  }

  public int get_work_time(){
    return WORK_TIME;
  }
  
  public double get_max_receive(){
    return ENERGY_PER_TICK;
  }

  public double get_idle_energy(){
    return IDLE_ENERGY;
  }
  
  public int get_power_time(){
    return POWER_ON_TIME;
  }

}
