package addsynth.energy.lib.gui.widgets;

import addsynth.core.gui.widgets.buttons.Checkbox;
import addsynth.energy.gameplay.NetworkHandler;
import addsynth.energy.lib.network_messages.ToggleAutoShutoffMessage;
import addsynth.energy.lib.tiles.machines.switchable.IAutoShutoff;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

/** A special checkbox that toggles the {@code auto_shutoff} state of a
 *  TileEntity that implements the {@link IAutoShutoff} interface. */
public final class AutoShutoffCheckbox<T extends BlockEntity & IAutoShutoff> extends Checkbox {

  private static final Component auto_shutoff_checkbox = Component.translatable("gui.addsynth_energy.common.auto_shutoff");

  private final T tile;
  
  public AutoShutoffCheckbox(int x, int y, T tile){
    super(x, y, auto_shutoff_checkbox);
    this.tile = tile;
  }
  
  @Override
  protected boolean get_toggle_state(){
    return tile.getAutoShutoff();
  }
  
  @Override
  public void onPress(){
    NetworkHandler.INSTANCE.sendToServer(new ToggleAutoShutoffMessage(tile.getBlockPos()));
  }

}
