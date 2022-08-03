package unmineraft.unitems.consumable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;

public class Drugs {
    protected static String[] generalDescription = {ChatColor.GRAY + "Este item es " + ChatColor.RED + "ILEGAL" + ChatColor.GRAY + ", que no te atrapen con el",
            ChatColor.GRAY + "Su consumo otorga: "};

    protected static int TICKS_PER_SECOND = 20;

    public static int DURATION_EFFECT_IN_SECONDS = 60 * TICKS_PER_SECOND;

    public static ItemStack marihuana;

    public static void createMarihuana(){
        ItemStack item = new ItemStack(Material.SWEET_BERRIES, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GREEN + "Marihuana");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(generalDescription[0]);

        String effects = generalDescription[2] + "Regeneracion y Hambre";
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
