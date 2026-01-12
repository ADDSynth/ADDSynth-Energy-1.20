package addsynth.energy.lib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class MachineDataConfig extends MachineData {

  private final String section;
  private ForgeConfigSpec.IntValue    work_time_config;
  private ForgeConfigSpec.DoubleValue energy_usage_config;
  private ForgeConfigSpec.DoubleValue idle_energy_config;
  private ForgeConfigSpec.IntValue    power_on_time_config;

  public MachineDataConfig(final String machine_name, int default_work_time, double default_energy, double default_idle_energy, int default_power_on_time){
    super(MachineType.STANDARD, default_work_time, default_energy, default_idle_energy, default_power_on_time);
    this.section = machine_name;
  }

  public MachineDataConfig(final String machine_name, MachineType type, int default_work_time, double default_energy, double default_idle_energy, int default_power_on_time){
    super(type, default_work_time, default_energy, default_idle_energy, default_power_on_time);
    this.section = machine_name;
  }

  public final void build(final ForgeConfigSpec.Builder builder){
    builder.push(section);
    work_time_config     = builder.defineInRange("Work Time",               WORK_TIME,       0, Integer.MAX_VALUE);
    energy_usage_config  = builder.defineInRange("Maximum Energy Per Tick", ENERGY_PER_TICK, 0, Float.MAX_VALUE);
    if(type != MachineType.PASSIVE){
      idle_energy_config   = builder.defineInRange("Idle Energy Drain",     IDLE_ENERGY,     0, Float.MAX_VALUE);
    }
    if(type != MachineType.ALWAYS_ON){
      power_on_time_config = builder.defineInRange("Power Cycle Time",      POWER_ON_TIME,   0, Integer.MAX_VALUE);
    }
    builder.pop();
  }

  @Override
  public final int get_work_time(){
    return work_time_config.get();
  }
  
  @Override
  public final double get_max_receive(){
    return energy_usage_config.get();
  }
  
  @Override
  public final double get_idle_energy(){
    return idle_energy_config != null ? idle_energy_config.get() : 0;
  }
  
  @Override
  public final int get_power_time(){
    return power_on_time_config != null ? power_on_time_config.get() : 0;
  }

}
