package addsynth.energy.lib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class BatteryConfig {

  private final String section;
  private final int DEFAULT_CAPACITY;
  private final int DEFAULT_MAX_EXTRACT;
  private final int DEFAULT_MAX_RECEIVE;
  private ForgeConfigSpec.IntValue capacity;
  private ForgeConfigSpec.IntValue max_extract;
  private ForgeConfigSpec.IntValue max_receive;
  
  public BatteryConfig(final String section_name, final int capacity, final int extract_rate, final int receive_rate){
    this.section = section_name;
    DEFAULT_CAPACITY = capacity;
    DEFAULT_MAX_EXTRACT = extract_rate;
    DEFAULT_MAX_RECEIVE = receive_rate;
  }
  
  public final void build(final ForgeConfigSpec.Builder builder){
    builder.push(section);
    capacity = builder.defineInRange("Capacity", DEFAULT_CAPACITY, 0, Integer.MAX_VALUE);
    max_extract = builder.defineInRange("Max Extract", DEFAULT_MAX_EXTRACT, 0, Integer.MAX_VALUE);
    max_receive = builder.defineInRange("Max Receive", DEFAULT_MAX_RECEIVE, 0, Integer.MAX_VALUE);
    builder.pop();
  }
  
  public final int getCapacity(){
    return capacity.get();
  }
  
  public final int getMaxExtract(){
    return max_extract.get();
  }
  
  public final int getMaxReceive(){
    return max_receive.get();
  }

}
