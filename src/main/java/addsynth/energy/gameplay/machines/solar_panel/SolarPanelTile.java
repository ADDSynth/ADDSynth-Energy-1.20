package addsynth.energy.gameplay.machines.solar_panel;

import javax.annotation.Nullable;
import addsynth.core.block_network.IBlockNetworkUser;
import addsynth.energy.gameplay.config.Config;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SolarPanelTile extends BlockEntity implements IBlockNetworkUser<SolarPanelNetwork> {

  private SolarPanelNetwork network;

  private final Vec3 start_vector;
  private final double MAX_DISTANCE = 1_024;
  private       Vec3   end_vector;
  private double dust_multiplier;
  private double light_multiplier;
  private boolean blocked;
  private int life = Config.SOLAR_PANEL.max_life.get();

  private double height;

  public SolarPanelTile(BlockPos position, BlockState blockstate){
    super(Tiles.SOLAR_PANEL.get(), position, blockstate);
    start_vector = new Vec3(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5);
  }

  @Override
  public void onLoad(){
    final Level level = this.level;
    if(level != null){
      if(!level.isClientSide){
        height = level.getMaxBuildHeight() - start_vector.y;
      }
    }
  }

  @Override
  public void serverTick(ServerLevel level, BlockState blockstate){
    SolarPanelNetwork.handler.tick(network, level, this);
    // Handle life
    final boolean wet = level.isRainingAt(worldPosition.above()) || blockstate.getValue(BlockStateProperties.WATERLOGGED);
    final int max_life = Config.SOLAR_PANEL.max_life.get();
    final int life_sections = max_life / SolarPanel.max_dirt_level;
    if(wet){
      life = max_life;
    }
    else{
      if(Config.SOLAR_PANEL.decrease_life.get()){
        if(life > 0){
          life--;
        }
      }
    }
    final double double_life = (double)life;
    final int life_level = SolarPanel.max_dirt_level - (int)Math.round(double_life / life_sections);
    if(blockstate.getValue(SolarPanel.DIRT_LEVEL) != life_level){
      level.setBlockAndUpdate(worldPosition, blockstate.setValue(SolarPanel.DIRT_LEVEL, life_level));
    }
    dust_multiplier = Mth.lerp(double_life / max_life, Config.SOLAR_PANEL.dirty_modifier.get(), 1.0);
    setChanged();
  }

  @Override
  public void load(final CompoundTag nbt){
    super.load(nbt);
    life = nbt.getInt("dirt");
  }

  @Override
  protected void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    nbt.putInt("dirt", life);
  }
  
  /** Executed every tick for every Solar Panel in the network. You want
   *  them all going at once to get an accurate return of Energy. */
  public final double getEnergy(Level level, double phase, double time_multiplier, double thunder_multiplier){
    // Part 3: Calculate blocked
    if(Config.SOLAR_PANEL.checkHitscanBlocking()){
      light_multiplier = 1;
      final double length = -Math.tan((phase + 0.5) * Math.PI) * height; // https://www.desmos.com/calculator/7odrod62cp
      final double distance = Math.sqrt((height * height) + (length * length));
      if(distance <= MAX_DISTANCE){
        end_vector = new Vec3(start_vector.x + length, start_vector.y + height, start_vector.z); // why do I have to create a new one every tick?
      }
      else{
        final double x = Math.cos(phase * Math.PI) * MAX_DISTANCE;
        final double y = Math.sin(phase * Math.PI) * MAX_DISTANCE;
        end_vector = new Vec3(start_vector.x + x, start_vector.y + y, start_vector.z);
      }
      final ClipContext context = new ClipContext(start_vector, end_vector, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, null);
      final BlockHitResult hit_result = level.clip(context);
      blocked = hit_result.getType() == HitResult.Type.BLOCK;
      if(blocked){
        return 0;
      }
    }
    else{
      final int light = level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(worldPosition);
      light_multiplier = (double)light / 15;
      blocked = light < 15;
    }
    // Part 4: output energy
    return Config.SOLAR_PANEL.energy.get() * time_multiplier * thunder_multiplier * dust_multiplier * light_multiplier;
  }

  public final boolean isBlocked(){
    return blocked;
  }

  public final double getLight(){
    return light_multiplier;
  }

  @Override
  public void setBlockNetwork(SolarPanelNetwork network){
    this.network = network;
  }

  @Override
  @Nullable
  public SolarPanelNetwork getBlockNetwork(){
    return network;
  }

}
