package unmineraft.undrugs.items.consumable;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.builder.ItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DrugItem extends ItemBuilder{
    public static HashMap<String, ItemStack> itemMap = new HashMap<>();

    protected final FileConfiguration fileConfiguration;

    public DrugItem(UNDrugs plugin){
        super(plugin);
        this.fileConfiguration = plugin.getConfig();
    }

    private Set<String> getSectionDrugs(){
        ConfigurationSection drugsSection = this.fileConfiguration.getConfigurationSection("drugs");
        if (drugsSection == null) throw new NullPointerException("ERROR_20: DRUGS SECTION UNRECOGNIZED");

        return drugsSection.getKeys(false);
    }

    @Override
    protected ItemStack buildItem(String pathDrug){
        Material baseMaterial = super.getBaseMaterial(pathDrug);
        ItemStack item = new ItemStack(baseMaterial, 1);

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) throw new NullPointerException("ERROR_30: NULL META OF BASE ITEM STACK");

        String displayName = super.getDisplayName(pathDrug);
        itemMeta.setDisplayName(displayName);

        List<String> lore = super.buildLore(pathDrug);
        if (lore == null) throw new NullPointerException("ERROR_40: NULL LORE REFERENCE");
        itemMeta.setLore(lore);

        itemMeta = super.addVisualEffects(itemMeta);

        if (itemMeta == null) throw new NullPointerException("ERROR_50: NULL ITEM META WITH VISUAL EFFECTS");

        // The method is generalized to facilitate the creation of base items
        if (super.checkExistence(pathDrug + ".customModelData")) {
            int customModelData = super.getCustomModelData(pathDrug);
            itemMeta.setCustomModelData(customModelData);
        }

        item.setItemMeta(itemMeta);

        return item;
    }

    @Override
    protected void storeItem(String itemName, ItemStack itemReference){
        DrugItem.itemMap.put(itemName, itemReference);
    }

    public void initItems(){
        Set<String> sectionNames = this.getSectionDrugs();
        String path;

        for (String drugName : sectionNames){
            path = "drugs." + drugName;
            this.storeItem(drugName, this.buildItem(path));
        }
    }
}
