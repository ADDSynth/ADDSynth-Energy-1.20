package addsynth.energy.gameplay.machines.solar_panel;

import javax.annotation.Nullable;
import addsynth.core.game.blocks.TileEntityBlock;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SolarPanel extends TileEntityBlock implements SimpleWaterloggedBlock {

  public static final int max_dirt_level = 8;
  public static final IntegerProperty DIRT_LEVEL = IntegerProperty.create("dirt_level", 0, max_dirt_level);
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

  public SolarPanel(){
    super(Block.Properties.of().mapColor(MapColor.WATER).strength(0.5f, 1.0f));
    registerDefaultState(stateDefinition.any().setValue(DIRT_LEVEL, 0).setValue(WATERLOGGED, false));
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos position, BlockState blockstate){
    return new SolarPanelTile(position, blockstate);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockstate, BlockEntityType<T> type){
    return standardTicker(level, type, Tiles.SOLAR_PANEL.get());
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext){
    return SHAPE;
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext){
    return SHAPE;
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState blockstate, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos){
    if(blockstate.getValue(WATERLOGGED)){
      world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    return blockstate;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean canSurvive(BlockState blockstate, LevelReader level, BlockPos position){
    final BlockPos below = position.below();
    return level.getBlockState(position.below()).isFaceSturdy(level, below, Direction.UP);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos){
    return !state.getValue(WATERLOGGED);
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state){
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
    builder.add(DIRT_LEVEL, WATERLOGGED);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving){
    SolarPanelNetwork.handler.onRemove(super::onRemove, state, world, pos, newState, isMoving);
  }

}
