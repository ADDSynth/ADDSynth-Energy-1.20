package addsynth.energy.lib.tiles.machines.switchable;

/** This interface is used on TileEntities that have an
 *  {@link addsynth.energy.lib.gui.widgets.AutoShutoffCheckbox Auto Shutoff checkbox}
 *  and want to be able to shut themselves off after performing work. */
public interface IAutoShutoff extends ISwitchableMachine {

  public void toggle_auto_shutoff();

  public boolean getAutoShutoff();

}
