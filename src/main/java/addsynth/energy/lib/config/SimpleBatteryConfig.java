package addsynth.energy.lib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class SimpleBatteryConfig {

  private final String section;
  private final int DEFAULT_CAPACITY;
  private final int DEFAULT_TRANSFER_RATE;
  private ForgeConfigSpec.IntValue capacity;
  private ForgeConfigSpec.IntValue max_transfer;
  
  public SimpleBatteryConfig(final String section_name, final int capacity, final int max_transfer_rate){
    this.section = section_name;
    DEFAULT_CAPACITY = capacity;
    DEFAULT_TRANSFER_RATE = max_transfer_rate;
  }
  
  public final void build(final ForgeConfigSpec.Builder builder){
    builder.push(section);
    capacity = builder.defineInRange("Capacity", DEFAULT_CAPACITY, 0, Integer.MAX_VALUE);
    max_transfer = builder.defineInRange("Max Transfer Rate", DEFAULT_TRANSFER_RATE, 0, Integer.MAX_VALUE);
    builder.pop();
  }
  
  public final int getCapacity(){
    return capacity.get();
  }
  
  public final int getMaxTransferRate(){
    return max_transfer.get();
  }
  
}
