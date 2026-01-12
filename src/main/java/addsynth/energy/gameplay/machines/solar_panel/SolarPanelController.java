package addsynth.energy.gameplay.machines.solar_panel;

import javax.annotation.Nullable;
import addsynth.core.game.blocks.TileEntityBlock;
import addsynth.core.util.game.MinecraftUtility;
import addsynth.energy.gameplay.client.GuiProvider;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class SolarPanelController extends TileEntityBlock {

  public SolarPanelController(){
    super(Block.Properties.of().mapColor(MapColor.WOOL).sound(SoundType.METAL).strength(0.5f, 6.0f));
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos position, BlockState blockstate){
    return new SolarPanelControllerTile(position, blockstate);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockstate, BlockEntityType<T> type){
    return standardTicker(level, type, Tiles.SOLAR_PANEL_CONTROLLER.get());
  }

  @Override
  @Deprecated
  public InteractionResult use(BlockState blockstate, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hit_result){
    if(level.isClientSide){
      final SolarPanelControllerTile tile = MinecraftUtility.getTileEntity(position, level, SolarPanelControllerTile.class);
      if(tile != null){
        GuiProvider.openSolarPanelController(tile, getName());
      }
    }
    return InteractionResult.SUCCESS;
  }

}
