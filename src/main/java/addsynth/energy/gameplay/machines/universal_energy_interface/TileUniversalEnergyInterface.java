package addsynth.energy.gameplay.machines.universal_energy_interface;

import addsynth.energy.compat.energy.EnergyCompat;
import addsynth.energy.compat.energy.forge.ForgeEnergyIntermediary;
import addsynth.energy.gameplay.config.Config;
import addsynth.energy.lib.energy_network.EnergyTransferStage;
import addsynth.energy.lib.main.Energy;
import addsynth.energy.lib.tiles.battery.BasicEnergyTile;
import addsynth.energy.lib.tiles.battery.ICustomEnergyTile;
import addsynth.energy.registers.Tiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TileUniversalEnergyInterface extends BasicEnergyTile implements ICustomEnergyTile, MenuProvider {

  private final ForgeEnergyIntermediary forge_energy = new ForgeEnergyIntermediary(energy){
    @Override
    public boolean canExtract(){
      return super.canExtract() && transfer_settings.external_extract;
    }
    @Override
    public boolean canReceive(){
      return super.canReceive() && transfer_settings.external_receive;
    }
  };

  private final InterfaceTransferSettings transfer_settings = new InterfaceTransferSettings();

  public TileUniversalEnergyInterface(BlockPos position, BlockState blockstate){
    super(Tiles.UNIVERSAL_ENERGY_INTERFACE.get(), position, blockstate, new Energy(Config.universal_energy_interface_buffer.get()));
  }

  @Override
  public final void derivedTick(ServerLevel level, BlockState blockstate){
    if(energy.getCapacity() != Config.universal_energy_interface_buffer.get()){
      energy.setCapacity(Config.universal_energy_interface_buffer.get());
    }
    final EnergyCompat.CompatEnergyNode[] energy_nodes = EnergyCompat.getConnectedEnergy(worldPosition, level);
    if(energy_nodes.length > 0){
      if(transfer_settings.active_pull){
        EnergyCompat.acceptEnergy(energy_nodes, energy);
      }
      if(transfer_settings.active_push){
        EnergyCompat.transmitEnergy(energy_nodes, energy);
      }
    }
  }

  @Override
  public final void load(final CompoundTag nbt){
    super.load(nbt);
    transfer_settings.load(nbt);
  }

  @Override
  protected final void saveAdditional(final CompoundTag nbt){
    super.saveAdditional(nbt);
    transfer_settings.save(nbt);
  }

  @Override
  @NotNull
  public <T> LazyOptional<T> getCapability(final @NotNull Capability<T> capability, final @Nullable Direction facing){
    if(remove == false){
      if(capability == ForgeCapabilities.ENERGY){
        return forge_energy != null ? (LazyOptional.of(()->forge_energy)).cast() : LazyOptional.empty();
      }
      return super.getCapability(capability, facing);
    }
    return LazyOptional.empty();
  }

  public final void setTransferSettings(final int index){
    switch(index){
    case 0: transfer_settings.setBiDirectional(); break;
    case 1: transfer_settings.setExternalBattery(); break;
    case 2: transfer_settings.setInternalBattery(); break;
    case 3: transfer_settings.setExtract(); break;
    case 4: transfer_settings.setReceive(); break;
    case 5: transfer_settings.setNoTransfer(); break;
    }
    changed = true;
  }

  public final void toggleSetting(final int index){
    transfer_settings.toggle(index);
    changed = true;
  }

  public final boolean getToggle(final int index){
    return transfer_settings.get(index);
  }

  @Override
  public final double getAvailableEnergy(EnergyTransferStage generator_stage){
    if(transfer_settings.isFreeGenerator(generator_stage)){ return energy.getAvailableEnergy(); }
    if(transfer_settings.isGenerator(    generator_stage)){ return energy.getAvailableEnergy(); }
    if(transfer_settings.isBattery(      generator_stage)){ return energy.getAvailableEnergy(); }
    return 0;
  }

  @Override
  public final double getRequestedEnergy(EnergyTransferStage receiver_stage){
    if(transfer_settings.isReceiver(receiver_stage)){ return energy.getRequestedEnergy(); }
    if(transfer_settings.isBattery( receiver_stage)){ return energy.getRequestedEnergy(); }
    return 0;
  }

  @Override
  @Nullable
  public AbstractContainerMenu createMenu(int id, Inventory player_inventory, Player player){
    return new ContainerUniversalEnergyInterface(id, player_inventory, this);
  }

  @Override
  public Component getDisplayName(){
    return getBlockState().getBlock().getName();
  }

}
