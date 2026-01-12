package addsynth.energy.lib.tiles.behavior;

/*
Energy Network Tile
Energy Network Tile -> Has Energy
Has Energy -> Generator (Abstract)
Has Energy -> Generator With Input Inventory
Has Energy -> Battery
Has Energy -> Battery -> Custom transfer rules
Has Energy -> Machine With Storage Invnetory (TileSuspensionBridge, but also has a Bridge Network)
Has Energy -> Machine -> Work Machine -> 
Base Tile -> Tile that Has Block Network (TileLaserHousing and TileSuspensionBridge)

2 machines that simply has to have a certain amount of energy per tick to function, TileSuspensionBridge and TileMatterCompressor.

systems:
  Storage Inventory (machine changes state based on stored items)
  Input Inventory (consume)
  Input & Output Inventory (works on items in-place)
  Output Inventory (produces items)
  Machine Inventory

  Generator
  Machine (Receiver)
  Battery
  Custom Energy Rules

  Work Machine (Machine Inventory)
  Passive Machine (Output Inventory)
  
  
  Block Network
*/
