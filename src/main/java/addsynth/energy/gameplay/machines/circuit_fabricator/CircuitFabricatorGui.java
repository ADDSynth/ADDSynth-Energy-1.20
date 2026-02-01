package addsynth.energy.gameplay.machines.circuit_fabricator;

import addsynth.core.gui.section.GuiSection;
import addsynth.core.gui.widgets.item.IngredientWidgetGroup;
import addsynth.core.gui.widgets.scrollbar.ItemListEntry;
import addsynth.core.gui.widgets.scrollbar.ItemListScrollbar;
import addsynth.energy.gameplay.NetworkHandler;
import addsynth.energy.gameplay.machines.circuit_fabricator.recipe.CircuitFabricatorRecipes;
import addsynth.energy.gameplay.reference.GuiReference;
import addsynth.energy.gameplay.reference.EnergyText;
import addsynth.energy.lib.gui.GuiEnergyBase;
import addsynth.energy.lib.gui.widgets.WorkProgressBar;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class CircuitFabricatorGui extends GuiEnergyBase<TileCircuitFabricator, CircuitFabricatorContainer> {

  private static final Component insert_text = Component.translatable("gui.addsynth_energy.circuit_fabricator.insert");
  private static final Component   take_text = Component.translatable("gui.addsynth_energy.circuit_fabricator.take");

  private Component selected_item = Component.empty();

  private final WorkProgressBar work_progress_bar = new WorkProgressBar(267, 128, 60, 5, 8, 245);

  private static final int guiWidth = 337;
  private static final int guiHeight = 239;
  private static final int text_line = 17;
  private static final int text_line2 = 28;
  private static final int down_arrow_texture_x = 356;
  private static final int down_arrow_texture_y = 0;
  private static final int up_arrow_texture_x = 356;
  private static final int up_arrow_texture_y = 16;
  private static final int[] arrow_draw_x = {178, 196, 214, 232, 250, 268, 286, 304};
  private static final int arrow_draw_y = 45; // 11 above input
  private static final int[] ingredient_draw_x = {177, 195, 213, 231, 249, 267, 285, 303};
  private static final int ingredient_draw_y = 28; // 28 above input
  private static final int draw_working_x = 189;
  private static final int draw_working_y = 82;
  private int draw_i;
  
  private static final IngredientWidgetGroup recipe_ingredients = new IngredientWidgetGroup(8);
  private static int length;
  
  private static final int list_entries = 10;
  private static final GuiSection item_list_section = GuiSection.box(6, 39, 164, 39 + (list_entries * 18));
  private static final int[] list_entry_y = {39, 57, 75, 93, 111, 129, 147, 165, 183, 201};
  private final ItemListEntry[] item_list_entries = new ItemListEntry[list_entries];
  private ItemListScrollbar item_scrollbar;
  
  public CircuitFabricatorGui(final CircuitFabricatorContainer container, Inventory player_inventory, Component title){
    super(guiWidth, guiHeight, container, player_inventory, title, GuiReference.circuit_fabricator);
  }

  @Override
  protected final void init(){
    super.init();
    // construct item list entries
    int i;
    for(i = 0; i < list_entries; i++){
      item_list_entries[i] = new ItemListEntry(
        leftPos + 6,
        topPos + list_entry_y[i],
        item_list_section.width - 12, 18
      );
      addRenderableWidget(item_list_entries[i]);
    }
    // construct scrollbar
    item_scrollbar = new ItemListScrollbar(
      leftPos + 6 + item_list_section.width - 12,
      topPos + item_list_section.y,
      item_list_section.height,
      item_list_entries,
      CircuitFabricatorRecipes.getRecipes()
    );
    addRenderableWidget(item_scrollbar);
    
    // setup data
    tile.updateGui(); // update displayed recipe, in case player opens another Circuit Fabricator
    final ItemStack output = tile.getRecipeOutput();
    selected_item = Component.translatable(output.getDescriptionId());
    item_scrollbar.init(output);
    
    // set responder after setting the Scrollbar's Selected Index
    item_scrollbar.setResponder(this::onItemSelected);
    
    // Buttons
    addRenderableWidget(Button.builder(insert_text, (Button button) -> {
      NetworkHandler.INSTANCE.sendToServer(new CircuitFabricatorButtonMessage(tile.getBlockPos(), true));
    }).bounds(leftPos + 168, topPos + 122, 50, 16).build());
    final Button button = Button.builder(Component.empty(), (Button) -> {
      NetworkHandler.INSTANCE.sendToServer(new CircuitFabricatorButtonMessage(tile.getBlockPos(), false));
    }).bounds(leftPos + 222, topPos + 122, 40, 16).build();
    button.active = false;
    addRenderableWidget(button);
  }

  private final void onItemSelected(final ItemStack item, final int index){
    if(item != null){
      NetworkHandler.INSTANCE.sendToServer(new ChangeCircuitFabricatorRecipe(tile.getBlockPos(), item));
      selected_item = Component.translatable(item.getDescriptionId());
    }
  }

  /** Called when the player changes the selected recipe on the server side.
      Only updates the IngredientWidgets drawn ItemStacks.  */
  public static final void updateRecipeDisplay(final Ingredient[] recipe){
    recipe_ingredients.setRecipe(recipe);
    length = recipe_ingredients.getLength();
  }

  @Override
  public final void containerTick(){
    recipe_ingredients.tick();
  }

  @Override
  protected final void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY){
    draw_wide_background_texture(graphics);
    work_progress_bar.draw(graphics, this, tile);
    // draw arrows and ingredients
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    for(draw_i = 0; draw_i < length; draw_i++){
      graphics.blit(GUI_TEXTURE, leftPos + arrow_draw_x[draw_i], topPos + arrow_draw_y, 14, 8, down_arrow_texture_x, down_arrow_texture_y, 28, 16, 384, 256);
      recipe_ingredients.drawIngredient(graphics, draw_i, leftPos + ingredient_draw_x[draw_i], topPos + ingredient_draw_y);
    }
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, int mouseX, int mouseY){
    draw_title(graphics);
    draw_status(graphics, tile, text_line);
    draw_energy_usage(graphics, center_x, text_line);
    draw_text_left(graphics, EnergyText.selected_text.getString()+": "+selected_item.getString(), 6, text_line2);
    for(draw_i = 0; draw_i < 8; draw_i++){
      graphics.renderItem(tile.getWorkingInventory().getStackInSlot(draw_i), draw_working_x + ((draw_i % 4) * 17), draw_working_y + ((draw_i / 4) * 17));
    }
    draw_text_center(graphics, work_progress_bar.getWorkTimeProgress(), 298, 116);
    draw_time_left_center(graphics, tile, (center_x + guiWidth - 6) / 2, 145);
  }

  @Override
  protected final void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY){
    super.renderTooltip(graphics, mouseX, mouseY);
    // draw ingredient tooltips
    recipe_ingredients.drawTooltips(graphics, font, this, leftPos, ingredient_draw_x, topPos, ingredient_draw_y, mouseX, mouseY);
  }

}
