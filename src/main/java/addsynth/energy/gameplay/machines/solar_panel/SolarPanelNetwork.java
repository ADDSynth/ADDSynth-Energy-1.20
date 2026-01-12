package addsynth.energy.gameplay.machines.solar_panel;

import addsynth.core.block_network.BlockNetwork;
import addsynth.core.block_network.BlockNetworkHandler;
import addsynth.core.util.time.WorldTime;
import addsynth.energy.gameplay.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;

public class SolarPanelNetwork extends BlockNetwork<SolarPanelTile> {

  private int panels_blocked;
  private double total_energy;

  private double daytime;
  private double phase;
  private double time_multiplier;
  private double thunder_multiplier;

  public static final BlockNetworkHandler<SolarPanelTile, SolarPanelNetwork> handler = new BlockNetworkHandler<>(SolarPanelTile.class, SolarPanelNetwork::new);

  public SolarPanelNetwork(final BlockPos position){
    super(position, handler);
  }

  @Override
  protected void tick(ServerLevel level){
    panels_blocked = 0;
    total_energy = 0;
    // Part 1: Check if dimension even has sky
    final DimensionType dimension = level.dimensionType();
    if(dimension.hasCeiling() || !dimension.hasSkyLight()){
      return;
    }
    // Part 2: Calculate world multipliers
    daytime = level.getDayTime() % WorldTime.minecraft_day_in_ticks;
    if(daytime < WorldTime.day_length_in_ticks){
      phase = daytime / WorldTime.day_length_in_ticks;
      time_multiplier = Math.max(Math.sin(phase * Math.PI), 0);
      thunder_multiplier = level.isThundering() ? Config.SOLAR_PANEL.heavy_rain_modifier.get() : 1.0;
      // Part 3+: Loop all Solar Panels
      blocks.forAllTileEntities((SolarPanelTile solar_panel) -> {
        total_energy += solar_panel.getEnergy(level, phase, time_multiplier, thunder_multiplier);
        if(solar_panel.isBlocked()){
          panels_blocked++;
        }
      });
    }
    else{
      time_multiplier = 0;
    }
  }

  public final boolean isBlocked(){
    return panels_blocked > 0;
  }

  public final int getBlockedCount(){
    return panels_blocked;
  }

  public final double getBlockedPercentage(){
    return (double)panels_blocked / blocks.size();
  }

  public final double getEnergy(){
    return total_energy;
  }

  public final double getTheoreticalEnergy(){
    return Config.SOLAR_PANEL.energy.get() * blocks.size() * time_multiplier;
  }

  @Override
  protected void clear_custom_data(){
  }

}
