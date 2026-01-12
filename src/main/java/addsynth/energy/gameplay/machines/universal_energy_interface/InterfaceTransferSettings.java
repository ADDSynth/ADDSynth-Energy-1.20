package addsynth.energy.gameplay.machines.universal_energy_interface;

import addsynth.energy.lib.energy_network.EnergyTransferStage;
import net.minecraft.nbt.CompoundTag;

public final class InterfaceTransferSettings {

  public boolean external_receive;
  public boolean external_extract;
  public boolean active_push;
  public boolean active_pull;
  public boolean internal_receive;
  public boolean internal_extract;
  public boolean is_free_energy_source;
  
  public final void load(final CompoundTag tag){
    external_receive = tag.getBoolean("External Receive");
    external_extract = tag.getBoolean("External Extract");
    active_push = tag.getBoolean("Active Push");
    active_pull = tag.getBoolean("Avtive Pull");
    internal_receive = tag.getBoolean("Internal Receive");
    internal_extract = tag.getBoolean("Internal Extract");
    is_free_energy_source = tag.getBoolean("Free Energy Source");
  }
  
  public final void save(final CompoundTag tag){
    tag.putBoolean("External Receive", external_receive);
    tag.putBoolean("External Extract", external_extract);
    tag.putBoolean("Active Push", active_push);
    tag.putBoolean("Active Pull", active_pull);
    tag.putBoolean("Internal Receive", internal_receive);
    tag.putBoolean("Internal Extract", internal_extract);
    tag.putBoolean("Free Energy Source", is_free_energy_source);
  }
  
  public final void toggle(int index){
    switch(index){
    case 0: external_extract = !external_extract; break;
    case 1: active_push      = !active_push;      break;
    case 2: external_receive = !external_receive; break;
    case 3: active_pull      = !active_pull;      break;
    case 4: internal_receive = !internal_receive; break;
    case 5: internal_extract = !internal_extract; break;
    case 6: is_free_energy_source = !is_free_energy_source; break;
    }
  }
  
  public final boolean get(final int index){
    return switch(index){
    case 0 -> external_extract;
    case 1 -> active_push;
    case 2 -> external_receive;
    case 3 -> active_pull;
    case 4 -> internal_receive;
    case 5 -> internal_extract;
    case 6 -> is_free_energy_source;
    default -> false;
    };
  }
  
  public final void setBiDirectional(){
    external_extract = true;
    external_receive = true;
    internal_extract = true;
    internal_receive = true;
  }

  public final void setExtract(){
    external_extract = true;
    internal_receive = true;
    external_receive = false;
    internal_extract = false;
  }
  
  public final void setReceive(){
    external_receive = true;
    external_extract = false;
    internal_receive = false;
    internal_extract = true;
  }
  
  public final void setExternalBattery(){
    external_receive = true;
    external_extract = true;
    internal_receive = false;
    internal_extract = false;
  }
  
  public final void setInternalBattery(){
    external_receive = false;
    external_extract = false;
    internal_receive = true;
    internal_extract = true;
    active_push = false;
    active_pull = false;
  }
  
  public final void setNoTransfer(){
    external_receive = false;
    external_extract = false;
    active_push = false;
    active_pull = false;
    internal_receive = false;
    internal_extract = false;
  }
  
  public final boolean isGenerator(final EnergyTransferStage stage){
    return stage == EnergyTransferStage.GENERATOR && internal_extract && !internal_receive && !is_free_energy_source;
  }
  
  public final boolean isFreeGenerator(final EnergyTransferStage stage){
    return stage == EnergyTransferStage.FREE_GENERATOR && internal_extract && !internal_receive && is_free_energy_source;
  }
  
  public final boolean isReceiver(final EnergyTransferStage stage){
    return stage == EnergyTransferStage.RECEIVER && !internal_extract && internal_receive;
  }
  
  public final boolean isBattery(final EnergyTransferStage stage){
    return stage == EnergyTransferStage.BATTERY && internal_extract && internal_receive;
  }

}
