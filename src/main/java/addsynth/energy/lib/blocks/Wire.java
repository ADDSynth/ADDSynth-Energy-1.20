package addsynth.energy.lib.blocks;

import javax.annotation.Nullable;
import addsynth.core.game.blocks.TileEntityBlock;
import addsynth.core.util.block.BlockShape;
import addsynth.core.util.constants.DirectionConstant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class Wire extends TileEntityBlock implements SimpleWaterloggedBlock {

  // http://mcforge.readthedocs.io/en/latest/blocks/states/

  private static final BooleanProperty NORTH = BlockStateProperties.NORTH;
  private static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
  private static final BooleanProperty WEST  = BlockStateProperties.WEST;
  private static final BooleanProperty EAST  = BlockStateProperties.EAST;
  private static final BooleanProperty UP    = BlockStateProperties.UP;
  private static final BooleanProperty DOWN  = BlockStateProperties.DOWN;
  private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final double default_min_wire_size =  5.0 / 16;
  private static final double default_max_wire_size = 11.0 / 16;

  protected final VoxelShape[] shapes;

  public Wire(final Block.Properties properties){
    super(properties);
    shapes = makeShapes();
    this.registerDefaultState(this.stateDefinition.any()
       .setValue(NORTH, false).setValue(SOUTH, false).setValue(WEST, false).setValue(EAST, false).setValue(UP, false).setValue(DOWN, false)
       .setValue(WATERLOGGED, false));
  }

  /** Override this method in extended classes to assign different size shapes.
   *  The base Wire class automatically calls this to assign the shapes array.
   */
  protected VoxelShape[] makeShapes(){
    return BlockShape.createWireShapes(default_min_wire_size, default_max_wire_size);
  }

  @Override
  @Nullable
  @SuppressWarnings("resource")
  public final BlockState getStateForPlacement(final BlockPlaceContext context){
    return getState(defaultBlockState(), context.getLevel(), context.getClickedPos());
  }

  protected abstract boolean[] get_valid_sides(BlockGetter world, BlockPos pos);

  private final BlockState getState(final BlockState state, final LevelAccessor world, final BlockPos position){
    final boolean[] valid_sides = get_valid_sides(world, position);
    return state.setValue(DOWN,  valid_sides[DirectionConstant.DOWN ])
                .setValue(UP,    valid_sides[DirectionConstant.UP   ])
                .setValue(NORTH, valid_sides[DirectionConstant.NORTH])
                .setValue(SOUTH, valid_sides[DirectionConstant.SOUTH])
                .setValue(WEST,  valid_sides[DirectionConstant.WEST ])
                .setValue(EAST,  valid_sides[DirectionConstant.EAST ])
                .setValue(WATERLOGGED, world.getFluidState(position).getType() == Fluids.WATER);
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
    return shapes[BlockShape.getIndex(state)];
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
    return shapes[BlockShape.getIndex(state)];
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos){
    if(state.getValue(WATERLOGGED)){
      world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    return getState(state, world, currentPos);
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
    builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN, WATERLOGGED);
  }

}
