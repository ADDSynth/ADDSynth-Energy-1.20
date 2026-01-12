package addsynth.energy.gameplay.machines.charger;

import java.util.List;
import javax.annotation.Nullable;
import addsynth.core.util.game.MinecraftUtility;
import addsynth.energy.gameplay.reference.EnergyText;
import addsynth.energy.lib.blocks.EnergyMachineBlock;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public final class ChargerBlock extends EnergyMachineBlock {

  public ChargerBlock(){
    super(MapColor.WOOL);
  }

  @Override
  public final void appendHoverText(ItemStack itemstack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag){
    tooltip.add(EnergyText.energy_machine);
  }

  @Override
  @Nullable
  public final BlockEntity newBlockEntity(BlockPos position, BlockState blockstate){
    return new TileCharger(position, blockstate);
  }

  @Override
  @Nullable
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState blockstate, BlockEntityType<T> type){
    return standardTicker(world, type, Tiles.CHARGER.get());
  }

  @Override
  @SuppressWarnings("deprecation")
  public final InteractionResult use(BlockState blockstate, Level world, BlockPos position, Player player, InteractionHand hand, BlockHitResult hit_result){
    if(!world.isClientSide){
      final TileCharger tile = MinecraftUtility.getTileEntity(position, world, TileCharger.class);
      if(tile != null){
        NetworkHooks.openScreen((ServerPlayer)player, tile, position);
      }
    }
    return InteractionResult.SUCCESS;
  }

}
