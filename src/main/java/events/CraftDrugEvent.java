package events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.consumable.DrugItem;
import unmineraft.undrugs.items.craftBase.BaseItem;

public class CraftDrugEvent implements Listener {
    private UNDrugs plugin;

    public CraftDrugEvent(UNDrugs plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event){
        ItemStack itemResult = event.getInventory().getResult();
        if (itemResult == null) return;
        if (!DrugItem.itemMetaMap.containsValue(itemResult.getItemMeta())) return;

        CraftingInventory craftingInventory = event.getInventory();
        ItemStack[] items = craftingInventory.getMatrix();

        /* If there is an item in the crafting inventory that shares an ItemMeta with a base item
         * we cancel the change to air */
        for (ItemStack item : items){
            if (!BaseItem.baseItemMetaMap.containsValue(item.getItemMeta())) continue;
            return;
        }

        event.getInventory().setResult(new ItemStack(Material.AIR));
    }
}
