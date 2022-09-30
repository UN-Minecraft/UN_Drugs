package unmineraft.undrugs.items.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.craftBase.BaseItem;
import unmineraft.undrugs.utilities.GetterConfig;

import java.util.LinkedList;
import java.util.List;

public class RecipeBuilder extends GetterConfig {
    private final UNDrugs plugin;

    public RecipeBuilder(UNDrugs plugin){
        super(plugin);
        this.plugin = plugin;
    }

    public LinkedList<String> getPlainRecipe(String pathDrug){
        List<String> plainShape = this.getStringList(pathDrug + ".shape");
        LinkedList<String> baseRecipe = new LinkedList<>();

        for (String line : plainShape){
            baseRecipe.addLast(line);
        }

        return baseRecipe;
    }

    public void setMaterials(String pathDrug, ShapedRecipe shapedRecipe){
        List<String> ingredients = this.getStringList(pathDrug + ".ingredients");

        for (String line :  ingredients){
            char idChar = 0;
            Material keyMaterial = null;
            // Normal Item
            if (line.contains(";")){
                String[] definition = line.split(";");
                idChar = definition[0].charAt(0);
                keyMaterial = Material.getMaterial(definition[1]);
            }

            // Created Item
            BaseItem controllerBaseItem = new BaseItem(this.plugin);
            if (line.contains(",")){
                String[] definition = line.split(";");
                idChar = definition[0].charAt(0);
                keyMaterial = controllerBaseItem.getMaterialByName(definition[1].toLowerCase());
            }

            if (keyMaterial == null) continue;

            shapedRecipe.setIngredient(idChar, keyMaterial);
        }
    }

    public void buildRecipe(String pathDrug, ItemStack itemDrug){
        NamespacedKey key = new NamespacedKey(this.plugin, pathDrug);

        ShapedRecipe recipe = new ShapedRecipe(key, itemDrug);
        String[] plainRecipe = this.getPlainRecipe(pathDrug).toArray(new String[0]);
        recipe.shape(plainRecipe);

        this.setMaterials(pathDrug, recipe);
        Bukkit.addRecipe(recipe);
    }
}
