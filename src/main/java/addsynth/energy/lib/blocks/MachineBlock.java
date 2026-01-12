package addsynth.energy.lib.blocks;

import addsynth.core.game.blocks.TileEntityBlock;
import addsynth.core.game.inventory.IInventoryUser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/** This is your typical machine-type block with a silver appearance and metallic properties.
 * @author ADDSynth
 */
public abstract class MachineBlock extends TileEntityBlock {

  /** Specify your own Block Properties. Required if block is transparent! */
  public MachineBlock(final Block.Properties properties){
    super(properties);
  }

  /** Standard constructor. SoundType = Metal, and standard block hardness. */
  public MachineBlock(final MapColor color){
    super(Block.Properties.of().mapColor(color).sound(SoundType.METAL).strength(0.5f, 6.0f));
  }

  @Override
  @SuppressWarnings("deprecation")
   public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving){
    final BlockEntity tile = world.getBlockEntity(pos);
    super.onRemove(state, world, pos, newState, isMoving);
    if(tile != null){
      if(tile.isRemoved()){
        if(tile instanceof IInventoryUser){
          ((IInventoryUser)tile).drop_inventory();
        }
      }
    }
  }
  
}
