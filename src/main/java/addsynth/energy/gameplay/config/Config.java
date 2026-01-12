package addsynth.energy.gameplay.config;

import addsynth.energy.lib.config.MachineDataConfig;
import addsynth.energy.lib.config.MachineType;
import addsynth.energy.lib.config.SimpleBatteryConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

  // general
  public static ForgeConfigSpec.BooleanValue balance_batteries;

  public static final MachineDataConfig compressor         = new MachineDataConfig("Compressor",         MachineType.ALWAYS_ON,  200, 10, 0, 0); // 2,000
  public static final MachineDataConfig circuit_fabricator = new MachineDataConfig("Circuit Fabricator", MachineType.ALWAYS_ON, 1000, 25, 0, 0);

  public static class SOLAR_PANEL {
    
    public enum PhaseDisplay {DEGREES, PERCENTAGE, TICKS, TIME}
    public enum BlockingAlgorithm {Hitscan, Sunlight}
    
    public static ForgeConfigSpec.BooleanValue decrease_life;
    public static ForgeConfigSpec.DoubleValue dirty_modifier;
    public static ForgeConfigSpec.DoubleValue energy;
    public static ForgeConfigSpec.IntValue max_life;
    public static ForgeConfigSpec.DoubleValue heavy_rain_modifier;
    public static ForgeConfigSpec.EnumValue<PhaseDisplay> phase_display;
    public static ForgeConfigSpec.EnumValue<BlockingAlgorithm> blocking_algorithm;
    
    private static final int DEFAULT_TIME = 24_192_000;
    
    public static final void build(ForgeConfigSpec.Builder builder){
      builder.push("Solar Panel");
      decrease_life = builder.comment(
        "Enable whether Solar Panels will get dustier over time. How dirty a solar panel gets will affect it's\n"+
        "efficiency. You can clean solar panels by putting water on it or waiting until it rains."
      ).define("Solar Panels get Dirty Over Time", true);
      dirty_modifier = builder.comment(
        "How much will maximum dirtiness will affect efficiency, as a multiplier."
      ).defineInRange("Dust Efficiency Multiplier", 0.7, 0, 1.0);
      max_life = builder.comment(
        "The time it takes for solar panels to get from clean to 100% dirty, measured in ticks."
      ).defineInRange("Max Life", DEFAULT_TIME, 100, Integer.MAX_VALUE);
      energy = builder.defineInRange("Energy per Tick", 0.5, 0, Double.MAX_VALUE);
      heavy_rain_modifier = builder.defineInRange("Heavy Rain Efficiency Multiplier", 0.5, 0, 1.0);
      phase_display = builder.comment(
        "Client Only: Determines how the Phase in the Solar Panel Controller gui is displayed."
      ).defineEnum("Phase Display", PhaseDisplay.DEGREES);
      blocking_algorithm = builder.comment(
        "Hitscan: Uses a hitscan line to determine if a path to the sun is blocked. This is the most\n"+
        "  realistic, and also the most processor intensive. However, if the hitscan finds blocks which\n"+
        "  would normally allow light to pass through, such as Glass, it will consider that as blocked.\n"+
        "Sunlight: Energy generated will still be determined by the time of day, but will also be multiplied\n"+
        "  by the amount of sunlight hitting the solar panel. If any solar panel does not have full \n"+
        "  brightness, then the Solar Panel Controller will display the Blocked status. Although this\n"+
        "  tremendously saves on processing power, it won't be very realistic if you can surround the solar\n"+
        "  panel with stacks of blocks, and it'll still receive full power."
      ).defineEnum("Blocking Detection Method", BlockingAlgorithm.Hitscan);
      builder.pop();
    }
    
    public static final boolean checkHitscanBlocking(){
      return blocking_algorithm.get() == BlockingAlgorithm.Hitscan;
    }
  }

  public static final SimpleBatteryConfig energy_storage = new SimpleBatteryConfig("Energy Storage Block", 200_000, 100);

  private static final int DEFAULT_UNIVERSAL_ENERGY_INTERFACE_BUFFER = 1_000;
  public static ForgeConfigSpec.IntValue     universal_energy_interface_buffer;

  public Config(final ForgeConfigSpec.Builder builder){
  
    builder.push("General");
      balance_batteries = builder.define("Balance Battery Energy", false);
    builder.pop();
  
    compressor.build(builder);
    circuit_fabricator.build(builder);
    SOLAR_PANEL.build(builder);
    energy_storage.build(builder);
    
    builder.push("Universal Energy Interface");
      universal_energy_interface_buffer   = builder.defineInRange("Universal Energy Interface Buffer",
                                              DEFAULT_UNIVERSAL_ENERGY_INTERFACE_BUFFER, 0, Integer.MAX_VALUE);
    builder.pop();
  }

}
