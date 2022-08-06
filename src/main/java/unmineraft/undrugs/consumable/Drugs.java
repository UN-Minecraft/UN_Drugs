package unmineraft.undrugs.consumable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import unmineraft.undrugs.UNDrugs;


import java.io.Console;
import java.lang.reflect.Array;
import java.util.*;

/*
 * Para nuevos items es necesario incluirlos en el archivo de configuracion
 * y en el arreglo de pathDruds, con el mismo nombre que posee en la configuracion
 */

public class Drugs {
    private static String generalPath = "Config.Drugs";
    private static List<String> generalLore;
    private static FileConfiguration config;

    public static String[] pathDrugs = {"Marihuana", "Perico", "LSD"};
    public static HashMap<String, HashMap<String, Object>> drugsInformation = new HashMap<>();

    // Items
    public static ItemStack marihuana;
    public static ItemStack perico;
    public static ItemStack LSD;

    private static void updateGeneralLore(String path){
        try {
            List<String> lore = Objects.requireNonNull(config.getStringList(path));
            generalLore = lore;
        } catch (Error error){
            generalLore = Arrays.asList(new String[]{""});
        }
    }

    private static String getLabelEffects(String path){
        try {
            String label = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(path)));
            return label;
        } catch (Error error){
            return "";
        }
    }

    private static int getDuration(String path){
        int TICKS_PER_SECOND = 20;
        try {
            int seconds = Integer.parseInt(Objects.requireNonNull(config.getString(path)));
            return TICKS_PER_SECOND * seconds;
        } catch (Error error){
            return 0;
        }
    }

    private static LinkedList<PotionEffect> getEffects(String path, int secondsDuration){
        try {
            LinkedList<PotionEffect> auxListEffects = new LinkedList<>();
            for (String effectTypes : Objects.requireNonNull(config.getStringList(path))){
                String[] effectTypeInfo = effectTypes.split(";");

                int level = Integer.parseInt(effectTypeInfo[1]) - 1;
                String nameEffect = effectTypeInfo[0];

                PotionEffect temp = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(nameEffect)), secondsDuration, level);
                auxListEffects.push(temp);
            }

            return  auxListEffects;
        } catch (Error error){
            return new LinkedList<PotionEffect>();
        }
    }

    private static String getDisplayName(String path){
        try {
            String plainText = Objects.requireNonNull(config.getString(path));
            return ChatColor.translateAlternateColorCodes('&', plainText);
        } catch (Error error){
            return "";
        }
    }

    private static void updateDrugConfig(String pathNameDrug){
        HashMap<String, Object> drug = new HashMap<>();

        String path = generalPath + "." + pathNameDrug + ".Label_Effects";
         drug.put("labelEffects", getLabelEffects(path));

        path = generalPath + "." + pathNameDrug + ".Duration";
        int secondsDuration = getDuration(path);
        drug.put("effectsDuration", secondsDuration);

        path = generalPath + "." + pathNameDrug + ".Effects";
        drug.put("effects", getEffects(path, secondsDuration));

        // Se guarda la informacion en la variable de clase
        drugsInformation.put(pathNameDrug, drug);
    }

    protected static ItemStack createItem(Material BaseItem, String pathNameDrug){
        ItemStack item = new ItemStack(BaseItem, 1);
        ItemMeta meta = item.getItemMeta();

        String path = generalPath + "." + pathNameDrug + ".DisplayName";
        meta.setDisplayName(Drugs.getDisplayName(path));

        ArrayList<String> lore = new ArrayList<>();

        String line;
        for (int i=0; i<generalLore.size(); i++){
            line = generalLore.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line).replaceAll("%effectsDrug%", (String) drugsInformation.get(pathNameDrug).get("labelEffects"));
            lore.add(line);
        }

        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);

        return item;
    }

    public static void buildDrugs(UNDrugs plugin){
        Drugs.config = plugin.getConfig();
        Drugs.updateGeneralLore(generalPath + ".General_Description");

        for (String pathNameDrug : Drugs.pathDrugs) {
            updateDrugConfig(pathNameDrug);
        }

        Drugs.marihuana = createItem(Material.SWEET_BERRIES, "Marihuana");
        Drugs.perico = createItem(Material.SUGAR, "Perico");
        Drugs.LSD = createItem(Material.PAPER, "LSD");
    }
}
