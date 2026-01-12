package addsynth.energy.gameplay.machines.energy_diagnostics;

import addsynth.energy.lib.main.Energy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class EnergyDiagnosticData implements Comparable<EnergyDiagnosticData> {

  public Component name;
  public EnergyType type;
  public double energy;
  public double capacity;
  public double in;
  public double max_receive;
  public double out;
  public double max_transmit;
  public double transfer;

  public EnergyDiagnosticData(){
  }
  
  public EnergyDiagnosticData(final Component name){
    this.name = name;
  }
  
  public EnergyDiagnosticData(final FriendlyByteBuf data){
    name = data.readComponent();
    type = data.readEnum(EnergyType.class);
    energy       = data.readDouble();
    capacity     = data.readDouble();
    in           = data.readDouble();
    max_receive  = data.readDouble();
    out          = data.readDouble();
    max_transmit = data.readDouble();
    transfer = in - out;
  }

  public final void set(final BlockEntity tile, final Energy energy){
    this.name = tile.getBlockState().getBlock().getName();
    this.type = EnergyType.determine(tile);
    this.energy       = energy.getEnergy();
    this.capacity     = energy.getCapacity();
    this.in           = energy.get_energy_in();
    this.max_receive  = energy.getMaxReceive();
    this.out          = energy.get_energy_out();
    this.max_transmit = energy.getMaxExtract();
    transfer = in - out;
  }

  public final void set(final EnergyDiagnosticData data){
    this.name = data.name;
    this.type = data.type;
    this.energy       = data.energy;
    this.capacity     = data.capacity;
    this.in           = data.in;
    this.max_receive  = data.max_receive;
    this.out          = data.out;
    this.max_transmit = data.max_transmit;
    transfer = in - out;
  }

  public final void clear(){
    name = Component.empty();
    type = EnergyType.GENERATOR;
    energy = 0;
    capacity = 0;
    in = 0;
    max_receive = 0;
    out = 0;
    max_transmit = 0;
    transfer = 0;
  }

  public final void save(final FriendlyByteBuf data){
    data.writeComponent(name);
    data.writeEnum(type);
    data.writeDouble(energy);
    data.writeDouble(capacity);
    data.writeDouble(in);
    data.writeDouble(max_receive);
    data.writeDouble(out);
    data.writeDouble(max_transmit);
  }
  
  @Override
  public int compareTo(EnergyDiagnosticData o){
    return Integer.compare(type.order, o.type.order);
  }

}
