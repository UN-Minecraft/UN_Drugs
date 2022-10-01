package unmineraft.undrugs.items.craftBase;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.consumable.DrugItem;

import java.util.HashMap;
import java.util.Set;

public class BaseItem extends DrugItem {
    public static HashMap<String, ItemStack> baseItemMap = new HashMap<>();
    public static HashMap<String, ItemMeta> baseItemMetaMap = new HashMap<>();
    public static HashMap<String, Material> baseItemTypeMap = new HashMap<>();

    public BaseItem(UNDrugs plugin){
        super(plugin);
    }

    public Material getMaterialByName(String nameItem) {
        String itemPath = "baseItems." + nameItem;

        if (!super.checkExistence(itemPath + ".baseMaterial")) return null;
        return super.getBaseMaterial(itemPath);
    }

    private Set<String> getSectionBaseItems() {
        ConfigurationSection baseItemsSection = super.fileConfiguration.getConfigurationSection("baseItems");
        if (baseItemsSection == null) throw new NullPointerException("ERROR_60: BASE ITEMS SECTION UNRECOGNIZED");

        return baseItemsSection.getKeys(false);
    }

    @Override
    protected void storeItem(String itemName, ItemStack itemReference){
        BaseItem.baseItemMap.put(itemName, itemReference);
        BaseItem.baseItemMetaMap.put(itemName, itemReference.getItemMeta());
        BaseItem.baseItemTypeMap.put(itemName, itemReference.getType());
    }

    @Override
    public void initItems() {
        Set<String> sectionNames = this.getSectionBaseItems();
        String path;

        for (String itemName : sectionNames){
            path = "baseItems." + itemName;
            this.storeItem(itemName, super.buildItem(path));
        }
    }
}
