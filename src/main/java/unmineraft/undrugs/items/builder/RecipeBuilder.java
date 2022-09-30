package unmineraft.undrugs.items.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.consumable.DrugItem;
import unmineraft.undrugs.items.craftBase.BaseItem;
import unmineraft.undrugs.utilities.GetterConfig;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RecipeBuilder extends GetterConfig {
    protected final FileConfiguration fileConfiguration;

    public RecipeBuilder(UNDrugs plugin){
        super(plugin);
        this.fileConfiguration = plugin.getConfig();
    }

    private LinkedList<String> getPlainRecipe(String pathDrug){
        List<String> plainShape = this.getStringList(pathDrug + ".shape");
        LinkedList<String> baseRecipe = new LinkedList<>();

        for (String line : plainShape){
            baseRecipe.addLast(line);
        }

        return baseRecipe;
    }

    private void setMaterials(String pathDrug, ShapedRecipe shapedRecipe){
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
            BaseItem controllerBaseItem = new BaseItem(super.plugin);
            if (line.contains(",")){
                String[] definition = line.split(";");
                idChar = definition[0].charAt(0);
                keyMaterial = controllerBaseItem.getMaterialByName(definition[1].toLowerCase());
            }

            if (keyMaterial == null) continue;

            shapedRecipe.setIngredient(idChar, keyMaterial);
        }
    }

    protected void buildRecipe(String pathDrug, ItemStack itemDrug){
        NamespacedKey key = new NamespacedKey(super.plugin, pathDrug);

        ShapedRecipe recipe = new ShapedRecipe(key, itemDrug);
        String[] plainRecipe = this.getPlainRecipe(pathDrug).toArray(new String[0]);
        recipe.shape(plainRecipe);

        this.setMaterials(pathDrug, recipe);
        Bukkit.addRecipe(recipe);
    }

    public void initRecipes(){
        ConfigurationSection section = this.fileConfiguration.getConfigurationSection("drugs");
        if (section == null) throw new NullPointerException("ERROR_60: DRUGS SECTION UNRECOGNIZED");
        Set<String> sectionNames = section.getKeys(false);

        String pathDrug;
        for (String sectionName : sectionNames){
            pathDrug = "drugs." + sectionName;
            if (!super.checkExistence(pathDrug + ".ingredients")) continue;
            if (!super.checkExistence(pathDrug + ".shape")) continue;

            if (!DrugItem.itemMap.containsKey(sectionName)) continue;

            ItemStack itemDrug = DrugItem.itemMap.get(sectionName);
            this.buildRecipe(pathDrug, itemDrug);
        }
    }
}
