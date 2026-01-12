package addsynth.energy.lib.blocks;

import javax.annotation.Nullable;
import addsynth.core.game.blocks.TileEntityBlock;
import addsynth.core.util.block.BlockShape;
import addsynth.core.util.block.BlockStateGroup;
import addsynth.core.util.math.number.BinaryEncoder;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class WallAttachableWire extends TileEntityBlock implements SimpleWaterloggedBlock {

  private static final BooleanProperty SIDE_1 = BooleanProperty.create("side0");
  private static final BooleanProperty SIDE_2 = BooleanProperty.create("side1");
  private static final DirectionProperty DIRECTION_1 = DirectionProperty.create("direction0");
  private static final DirectionProperty DIRECTION_2 = DirectionProperty.create("direction1");
  private static final IntegerProperty WIRE_1 = IntegerProperty.create("wire0", 0, 15);
  private static final IntegerProperty WIRE_2 = IntegerProperty.create("wire1", 0, 15);
  private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final VoxelShape[] shapes = BlockShape.createPanelShapes(0.125);

  private static final int RIGHT = 0;
  private static final int UP    = 1;
  private static final int LEFT  = 2;
  private static final int DOWN  = 3;

  public WallAttachableWire(Block.Properties properties){
    super(properties.noOcclusion().noCollission());
    this.registerDefaultState(this.stateDefinition.any()
      .setValue(SIDE_1, true)
      .setValue(SIDE_2, false)
      .setValue(DIRECTION_1, Direction.DOWN)
      .setValue(DIRECTION_2, Direction.DOWN)
      .setValue(WIRE_1, 0)
      .setValue(WIRE_2, 0)
      .setValue(WATERLOGGED, false)
    );
  }

  /** Override this and return true for all blocks this wire should connect to.
   *  The wire block is already a valid block this wire can connect to. */
  protected abstract boolean isAcceptableBlock(Block block, BlockState blockstate);

  @Override
  @Nullable
  @SuppressWarnings("resource")
  public BlockState getStateForPlacement(BlockPlaceContext context){
    return updateBlockState(defaultBlockState(), context.getLevel(), context.getClickedPos());
  }

  private final BlockState updateBlockState(BlockState blockstate, LevelAccessor level, BlockPos position){
    // Step 1: Get All Data
    BlockStateGroup blockstateGroup = new BlockStateGroup(level, position);
    BlockPos side_position;
    boolean[] solid = new boolean[6];
    Direction[] all_directions = Direction.values();
    int index;
    for(Direction direction : all_directions){
      index = direction.get3DDataValue();
      side_position = position.relative(direction);
      solid[index] = blockstateGroup.get(side_position).isFaceSturdy(level, side_position, direction.getOpposite());
    }
    // Step 2: Reevaluate current sides
    if(blockstate.getValue(SIDE_1)){
      index = blockstate.getValue(DIRECTION_1).get3DDataValue();
      if(!solid[index]){
        blockstate = blockstate.setValue(SIDE_1, false);
      }
    }
    if(blockstate.getValue(SIDE_2)){
      index = blockstate.getValue(DIRECTION_2).get3DDataValue();
      if(!solid[index]){
        blockstate = blockstate.setValue(SIDE_2, false);
      }
    }
    // Step 3: Add new Sides
    for(Direction direction : all_directions){
      if(solid[direction.get3DDataValue()]){
        if(!blockstate.getValue(SIDE_1)){
          blockstate = blockstate.setValue(SIDE_1, true).setValue(DIRECTION_1, direction);
          continue;
        }
        if(!blockstate.getValue(SIDE_2)){
          blockstate = blockstate.setValue(SIDE_2, true).setValue(DIRECTION_2, direction);
        }
      }
    }
    // Step 4: Update Wires
    blockstate = checkBlockState(blockstate, SIDE_1, DIRECTION_1, WIRE_1, position, blockstateGroup, SIDE_2, DIRECTION_2);
    blockstate = checkBlockState(blockstate, SIDE_2, DIRECTION_2, WIRE_2, position, blockstateGroup, SIDE_1, DIRECTION_1);
    return blockstate;
  }

  private final BlockState checkBlockState(BlockState blockstate, BooleanProperty side, DirectionProperty direction, IntegerProperty wire, BlockPos position, BlockStateGroup blockstateGroup, BooleanProperty other_side, DirectionProperty other_direction){
    if(blockstate.getValue(side)){
      blockstate = switch(blockstate.getValue(direction)){
      case DOWN  -> checkSide(blockstate, wire, Direction.DOWN,  blockstateGroup, position, Direction.EAST,  Direction.NORTH, Direction.WEST,  Direction.SOUTH, other_side, other_direction);
      case EAST  -> checkSide(blockstate, wire, Direction.EAST,  blockstateGroup, position, Direction.SOUTH, Direction.UP,    Direction.NORTH, Direction.DOWN,  other_side, other_direction);
      case WEST  -> checkSide(blockstate, wire, Direction.WEST,  blockstateGroup, position, Direction.NORTH, Direction.UP,    Direction.SOUTH, Direction.DOWN,  other_side, other_direction);
      case NORTH -> checkSide(blockstate, wire, Direction.NORTH, blockstateGroup, position, Direction.EAST,  Direction.UP,    Direction.WEST,  Direction.DOWN,  other_side, other_direction);
      case SOUTH -> checkSide(blockstate, wire, Direction.SOUTH, blockstateGroup, position, Direction.EAST,  Direction.DOWN,  Direction.WEST,  Direction.UP,    other_side, other_direction);
      case UP    -> checkSide(blockstate, wire, Direction.UP,    blockstateGroup, position, Direction.EAST,  Direction.SOUTH, Direction.WEST,  Direction.NORTH, other_side, other_direction);
      };
    }
    return blockstate;
  }

  // This only needs to check ajacent or below. If there's a block above, the OTHER SIDE will check there.
  private final BlockState checkSide(BlockState blockstate, IntegerProperty wire, Direction face, BlockStateGroup blockstateGroup, BlockPos position, Direction right, Direction up, Direction left, Direction down, BooleanProperty other_side, DirectionProperty direction_property){
    int value = BinaryEncoder.encode(
      checkConnection(position, blockstateGroup, face, right),
      checkConnection(position, blockstateGroup, face, up),
      checkConnection(position, blockstateGroup, face, left),
      checkConnection(position, blockstateGroup, face, down)
    );
    if(blockstate.getValue(other_side)){
      Direction direction = blockstate.getValue(direction_property);
      if(direction == right){ value |= 1;}
      if(direction == up   ){ value |= 2;}
      if(direction == left ){ value |= 4;}
      if(direction == down ){ value |= 8;}
    }
    return blockstate.setValue(wire, value);
  }

  private final boolean checkConnection(BlockPos position, BlockStateGroup blockstateGroup, Direction face, Direction direction){
    BlockState state = blockstateGroup.get(position.relative(direction));
    if(isAcceptableBlock(state.getBlock(), state)){
      return true;
    }
    if(checkBlock(state, face)){
      return true;
    }
    state = blockstateGroup.get(position.relative(direction).relative(face)); // which is down
    return checkBlock(state, direction.getOpposite());
  }

  private final boolean checkBlock(BlockState state, Direction face){
    if(state.getBlock() == this){
      final boolean side1 = state.getValue(SIDE_1) && state.getValue(DIRECTION_1) == face;
      final boolean side2 = state.getValue(SIDE_2) && state.getValue(DIRECTION_2) == face;
      return side1 || side2;
    }
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(BlockState blockstate, BlockGetter worldIn, BlockPos pos, CollisionContext context){
    VoxelShape shape = Shapes.empty();
    Direction direction;
    if(blockstate.getValue(SIDE_1)){
      direction = blockstate.getValue(DIRECTION_1);
      shape = Shapes.or(shape, shapes[direction.get3DDataValue()]);
    }
    if(blockstate.getValue(SIDE_2)){
      direction = blockstate.getValue(DIRECTION_2);
      shape = Shapes.or(shape, shapes[direction.get3DDataValue()]);
    }
    return shape;
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context){
    return Shapes.empty();
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos){
    if(state.getValue(WATERLOGGED)){
      world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    return updateBlockState(state, world, currentPos);
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
    builder.add(SIDE_1, SIDE_2, DIRECTION_1, DIRECTION_2, WIRE_1, WIRE_2, WATERLOGGED);
  }

}
