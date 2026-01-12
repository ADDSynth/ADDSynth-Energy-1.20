package addsynth.energy.registers;

import addsynth.energy.gameplay.machines.charger.ChargerContainer;
import addsynth.energy.gameplay.machines.circuit_fabricator.CircuitFabricatorContainer;
import addsynth.energy.gameplay.machines.compressor.ContainerCompressor;
import addsynth.energy.gameplay.machines.electric_furnace.ContainerElectricFurnace;
import addsynth.energy.gameplay.machines.energy_storage.ContainerEnergyStorage;
import addsynth.energy.gameplay.machines.generator.ContainerGenerator;
import addsynth.energy.gameplay.machines.universal_energy_interface.ContainerUniversalEnergyInterface;
import addsynth.energy.gameplay.reference.Names;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class Containers {

  public static final RegistryObject<MenuType<ContainerGenerator>> GENERATOR =
    RegistryObject.create(Names.GENERATOR, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<ChargerContainer>> CHARGER =
    RegistryObject.create(Names.CHARGER, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<ContainerCompressor>> COMPRESSOR =
    RegistryObject.create(Names.COMPRESSOR, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<ContainerEnergyStorage>> ENERGY_STORAGE_CONTAINER =
    RegistryObject.create(Names.ENERGY_STORAGE, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<ContainerUniversalEnergyInterface>> UNIVERSAL_ENERGY_INTERFACE =
    RegistryObject.create(Names.UNIVERSAL_ENERGY_INTERFACE, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<ContainerElectricFurnace>> ELECTRIC_FURNACE =
    RegistryObject.create(Names.ELECTRIC_FURNACE, ForgeRegistries.MENU_TYPES);

  public static final RegistryObject<MenuType<CircuitFabricatorContainer>> CIRCUIT_FABRICATOR =
    RegistryObject.create(Names.CIRCUIT_FABRICATOR, ForgeRegistries.MENU_TYPES);

}
