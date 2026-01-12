package addsynth.energy.gameplay.machines.compressor;

import java.util.Random;
import javax.annotation.Nullable;
import addsynth.core.util.math.random.RandomUtil;
import addsynth.energy.gameplay.config.Config;
import addsynth.energy.gameplay.machines.compressor.recipe.CompressorRecipes;
import addsynth.energy.lib.tiles.machines.TileStandardWorkMachine;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public final class TileCompressor extends TileStandardWorkMachine implements MenuProvider {

  private int compress_step = -1;
  private int temp_compress_step;
  private static final int max_compress_steps = 3;
  private static final Random random = new Random();

  public TileCompressor(BlockPos position, BlockState blockstate){
    super(Tiles.COMPRESSOR.get(), position, blockstate, 1, CompressorRecipes.INSTANCE.getFilter(), 1, Config.compressor);
    inventory.setRecipeProvider(CompressorRecipes.INSTANCE);
  }

  @Override
  protected final void machine_running(){
    temp_compress_step = calculate_compress_step();
    if(temp_compress_step != compress_step){
      compress_step = temp_compress_step;
      // 0.5 is the lowest pitch that can be produced using Minecraft's code
      final float pitch = RandomUtil.getCentralDistribution(random, 0.05f, 0.53f); // range is between 0.48 and 0.58
      level.playSound(null, worldPosition, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.25f, pitch);
    }
  }

  private final int calculate_compress_step(){
    return (int)((energy.getEnergy() * max_compress_steps) / energy.getCapacity());
  }

  @Override
  public final void onLoad(){
    super.onLoad();
    compress_step = calculate_compress_step();
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, Inventory player_inventory, Player player){
    return new ContainerCompressor(id, player_inventory, this);
  }

  @Override
  public Component getDisplayName(){
    return getBlockState().getBlock().getName();
  }

}
