package events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import unmineraft.undrugs.items.consumable.DrugItem;
import unmineraft.undrugs.items.craftBase.BaseItem;

public class CraftDrugEvent implements Listener {
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event){
        CraftingInventory craftingInventory = event.getInventory();

        ItemStack itemResult = craftingInventory.getResult();
        if (itemResult == null) return;

        if (!DrugItem.itemMetaMap.containsValue(itemResult.getItemMeta())) return;

        ItemStack[] items = craftingInventory.getMatrix();

        /* If there is an item in the crafting inventory that shares an ItemMeta with a base item
         * we cancel the change to air */
        for (ItemStack item : items){
            if (item == null) continue;
            if (!BaseItem.baseItemTypeMap.containsValue(item.getType())) continue;
            if (!BaseItem.baseItemMetaMap.containsValue(item.getItemMeta())) continue;
            return;
        }

        craftingInventory.setResult(new ItemStack(Material.AIR));
    }
}
