package unmineraft.unitems.consumable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import unmineraft.unitems.UNItems;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Drugs {
    protected static int TICKS_PER_SECOND = 20;

    private static String generalPath;
    protected static List<String> generalDescription;
    public static HashMap<String, String> labelEffects = new HashMap<>();
    public static HashMap<String, Integer> effectsDuration = new HashMap<>();

    protected static void updateMarihuanaConfig(FileConfiguration config){
        // Label Effects
        String plainText = config.getString(generalPath + ".marihuana.Label_Effects");
        String label = ChatColor.translateAlternateColorCodes('&', plainText);

        labelEffects.put("Marihuana", label);

        // Duration
        int secondsDuration = Integer.parseInt(Objects.requireNonNull(config.getString(generalPath + ".marihuana.Duration")));
        effectsDuration.put("Marihuana", secondsDuration * TICKS_PER_SECOND);
    }

    public static ItemStack marihuana;

    protected static void createMarihuana(){
        ItemStack item = new ItemStack(Material.SWEET_BERRIES, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.DARK_GREEN + "Marihuana");

        ArrayList<String> lore = new ArrayList<>();

        String line;
        for (int i=0; i<generalDescription.size(); i++){
            line = generalDescription.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line).replaceAll("%effectsDrug%", labelEffects.get("Marihuana"));
            lore.add(line);
        }

        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        marihuana = item;
    }


    public static void buildDrugs(UNItems plugin){
        // Actualizacion de la informacion general
        FileConfiguration config = plugin.getConfig();

        generalPath = "Config.Drugs";
        generalDescription = config.getStringList(generalPath + ".General_Description");

        // Actualizacion de la configuracion de los items
        updateMarihuanaConfig(config);

        createMarihuana();
    }
}
