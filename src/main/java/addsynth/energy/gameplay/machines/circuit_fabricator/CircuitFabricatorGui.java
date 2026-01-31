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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class CircuitFabricatorGui extends GuiEnergyBase<TileCircuitFabricator, CircuitFabricatorContainer> {

  private Component selected_item = Component.empty();

  private final WorkProgressBar work_progress_bar = new WorkProgressBar(239, 125, 58, 5, 8, 245);

  private static final int down_arrow_texture_x = 228;
  private static final int down_arrow_texture_y = 201;
  private static final int up_arrow_texture_x = 228;
  private static final int up_arrow_texture_y = 217;
  private static final int[] arrow_draw_x = {163, 181, 199, 217};
  private static final int[] arrow_draw_y = {65, 113};
  private static final int[] ingredient_draw_x = {162, 180, 198, 216, 162, 180, 198, 216};
  private static final int[] ingredient_draw_y = {48, 48, 48, 48, 122, 122, 122, 122};
  
  private static final IngredientWidgetGroup recipe_ingredients = new IngredientWidgetGroup(8);
  
  private static final GuiSection item_list_section = GuiSection.box(6, 50, 157, 140);
  private static final int[] list_entry_y = {50, 68, 86, 104, 122};
  private final ItemListEntry[] item_list_entries = new ItemListEntry[5];
  private ItemListScrollbar item_scrollbar;
  
  public CircuitFabricatorGui(final CircuitFabricatorContainer container, Inventory player_inventory, Component title){
    super(317, 239, container, player_inventory, title, GuiReference.circuit_fabricator);
  }

  @Override
  protected final void init(){
    super.init();
    // construct item list entries
    int i;
    for(i = 0; i < 5; i++){
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
    final int length = recipe_ingredients.getLength();
    if(length >= 1){
      draw(graphics, arrow_draw_x[0], arrow_draw_y[0], down_arrow_texture_x, down_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 0, leftPos + ingredient_draw_x[0], topPos + ingredient_draw_y[0]);
    }
    if(length >= 2){
      draw(graphics, arrow_draw_x[1], arrow_draw_y[0], down_arrow_texture_x, down_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 1, leftPos + ingredient_draw_x[1], topPos + ingredient_draw_y[1]);
    }
    if(length >= 3){
      draw(graphics, arrow_draw_x[2], arrow_draw_y[0], down_arrow_texture_x, down_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 2, leftPos + ingredient_draw_x[2], topPos + ingredient_draw_y[2]);
    }
    if(length >= 4){
      draw(graphics, arrow_draw_x[3], arrow_draw_y[0], down_arrow_texture_x, down_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 3, leftPos + ingredient_draw_x[3], topPos + ingredient_draw_y[3]);
    }
    if(length >= 5){
      draw(graphics, arrow_draw_x[0], arrow_draw_y[1],   up_arrow_texture_x,   up_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 4, leftPos + ingredient_draw_x[4], topPos + ingredient_draw_y[4]);
    }
    if(length >= 6){
      draw(graphics, arrow_draw_x[1], arrow_draw_y[1],   up_arrow_texture_x,   up_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 5, leftPos + ingredient_draw_x[5], topPos + ingredient_draw_y[5]);
    }
    if(length >= 7){
      draw(graphics, arrow_draw_x[2], arrow_draw_y[1],   up_arrow_texture_x,   up_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 6, leftPos + ingredient_draw_x[6], topPos + ingredient_draw_y[6]);
    }
    if(length >= 8){
      draw(graphics, arrow_draw_x[3], arrow_draw_y[1],   up_arrow_texture_x,   up_arrow_texture_y, 14, 8, 28, 16);
      recipe_ingredients.drawIngredient(graphics, 7, leftPos + ingredient_draw_x[7], topPos + ingredient_draw_y[7]);
    }
  }

  @Override
  protected final void renderLabels(GuiGraphics graphics, int mouseX, int mouseY){
    draw_title(graphics);
    draw_energy_usage(graphics);
    draw_status(graphics, tile);
    draw_text_left(graphics, EnergyText.selected_text.getString()+": "+selected_item.getString(), 6, 39);
    // itemRenderer.renderGuiItem(circuit_stack[tile.getCircuitID()], 102, 29);
    draw_text_center(graphics, work_progress_bar.getWorkTimeProgress(), 270, 113);
    draw_time_left_center(graphics, tile, 145);
  }

  @Override
  protected final void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY){
    super.renderTooltip(graphics, mouseX, mouseY);
    // draw ingredient tooltips
    recipe_ingredients.drawTooltips(graphics, font, this, leftPos, ingredient_draw_x, topPos, ingredient_draw_y, mouseX, mouseY);
  }

}
