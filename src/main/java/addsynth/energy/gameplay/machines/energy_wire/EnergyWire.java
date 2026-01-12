package addsynth.energy.gameplay.machines.energy_wire;

import javax.annotation.Nullable;
import addsynth.energy.gameplay.machines.energy_diagnostics.TileEnergyDiagnostics;
import addsynth.energy.lib.blocks.Wire;
import addsynth.energy.lib.energy_network.EnergyNetwork;
import addsynth.energy.lib.main.IEnergyUser;
import addsynth.energy.lib.tiles.AbstractEnergyNetworkTile;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public final class EnergyWire extends Wire {

  public EnergyWire(){
    super(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.1f, 0.0f));
  }

  @Override
  protected final boolean[] get_valid_sides(final BlockGetter world, final BlockPos pos){
    final boolean[] valid_sides = new boolean[6];
    for(Direction side : Direction.values()){
      valid_sides[side.ordinal()] = false;
      final BlockEntity tile = world.getBlockEntity(pos.relative(side));
      if(tile != null){
        if(tile instanceof AbstractEnergyNetworkTile || tile instanceof IEnergyUser || tile instanceof TileEnergyDiagnostics){
          valid_sides[side.ordinal()] = true;
        }
      }
    }
    return valid_sides;
  }

  @Override
  @Nullable
  public final BlockEntity newBlockEntity(BlockPos position, BlockState blockstate){
    return new TileEnergyWire(position, blockstate);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockstate, BlockEntityType<T> type){
    return standardTicker(world, type, Tiles.ENERGY_WIRE.get());
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving){
    EnergyNetwork.handler.onRemove(super::onRemove, state, world, pos, newState, isMoving);
  }

  @Override
  @SuppressWarnings("deprecation")
  public final void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos neighbor, boolean isMoving){
    EnergyNetwork.handler.neighbor_changed(world, pos, neighbor);
  }

}
