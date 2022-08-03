package unmineraft.unitems.consumable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Drugs {
    protected static String[] generalDescription = {ChatColor.GRAY + "Este item es " + ChatColor.RED + "ILEGAL" + ChatColor.GRAY + " que no te",
            ChatColor.GRAY + "atrapen con su posesion", ChatColor.GRAY + "Su consumo otorga: "};


    public static ItemStack marihuana;

    public static void createMarihuana(){
        ItemStack item = new ItemStack(Material.SWEET_BERRIES, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Marihuana");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(generalDescription[0]);
        lore.add(generalDescription[1]);

        String effects = lore.add(generalDescription[2]) + "Regeneracion y Hambre";
        lore.add(effects);
        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        marihuana = item;
    }

    public static void buildDrugs(){
        createMarihuana();
    }
}
