package events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.consumable.Drugs;
import unmineraft.undrugs.craftBase.BaseItemCraft;

import java.util.Objects;

public class CraftDrugEvent implements Listener {
    private static UNDrugs plugin;

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event){
        if (event.getInventory().getResult() == null) return;


        // Manejo del cogote como elemento válido para la marihuana
        if (event.getInventory().getResult().getType() == Drugs.marihuana.getType()){
            CraftingInventory inventoryCraft = event.getInventory();

            ItemStack[] items = inventoryCraft.getMatrix();

            int[] positions = {3, 4, 5};
            boolean isValidBeetroot = true;

            /*
             * El cogote de manera forzosa debe estar en el medio, asi que evaluamos
             * todas las posiciones posibles, si el item es del mismo tipo, pero no
             * tiene el mismo meta, se asume que no es el cogote y por ende es inválido
             */
            for (int pos : positions){
                if (items[pos] != null) {
                    if (items[pos].getType() == BaseItemCraft.cogote.getType()) {
                        // Verificamos que los display names sean iguales
                        String itemDisplayName = Objects.requireNonNull(items[pos].getItemMeta()).getDisplayName();
                        String cogoteDisplayName = Objects.requireNonNull(BaseItemCraft.cogote.getItemMeta()).getDisplayName();

                        if (!(itemDisplayName.equals(cogoteDisplayName))) isValidBeetroot = false;
                    }
                }
            }

            // Si no es válido, no generara la marihuana
            if (!isValidBeetroot){
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    public CraftDrugEvent(UNDrugs plugin){
        CraftDrugEvent.plugin = plugin;
    }
}
