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


import java.util.*;

public class Drugs {
    protected static int TICKS_PER_SECOND = 20;

    private static String generalPath;
    protected static List<String> generalDescription;

    // Mapeo de las diferentes caracteristicas de cada droga
    public static  HashMap<String, LinkedList<PotionEffect>> effectsMap = new HashMap<>();
    public static HashMap<String, String> labelEffects = new HashMap<>();
    public static HashMap<String, Integer> effectsDuration = new HashMap<>();

    // ITEMS
    public static ItemStack marihuana;
    public static ItemStack perico;
    public static ItemStack LSD;

    // Lectura y almacenamiento de la informacion de la droga
    private static void updateDrugConfig(FileConfiguration config, String mapNameDrug, String pathNameDrug){
        // Efectos en etiqueta
        String plainText = config.getString(generalPath + "." + pathNameDrug + ".Label_Effects");
        String label = ChatColor.translateAlternateColorCodes('&', plainText);

        labelEffects.put(mapNameDrug, label);

        // Duracion
        int secondsDuration = Integer.parseInt(Objects.requireNonNull(config.getString(generalPath + "." + pathNameDrug + ".Duration")));
        effectsDuration.put(mapNameDrug, secondsDuration * TICKS_PER_SECOND);

        // Efectos
        LinkedList<PotionEffect> auxListEffects = new LinkedList<>();

        for (String effectTypes : config.getStringList(generalPath + "." + pathNameDrug + ".Effects")){
            String[] effectTypeInfo = effectTypes.split(";");

            int level = Integer.parseInt(effectTypeInfo[1]) - 1;
            String nameEffect = effectTypeInfo[0];

            PotionEffect temp = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(nameEffect)), effectsDuration.get(mapNameDrug), level);
            auxListEffects.push(temp);
        }
        effectsMap.put(mapNameDrug, auxListEffects);
    }

    // Creacion del item y asignacion a la variable estatica
    protected static ItemStack createItem(Material BaseItem, String displayName, String mapNameDrug){
        ItemStack item = new ItemStack(BaseItem, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName);

        ArrayList<String> lore = new ArrayList<>();

        String line;
        for (int i=0; i<generalDescription.size(); i++){
            line = generalDescription.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line).replaceAll("%effectsDrug%", labelEffects.get(mapNameDrug));
            lore.add(line);
        }

        meta.setLore(lore);

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);

        return item;
    }


    // Funcion de inicializacion
    public static void buildDrugs(UNDrugs plugin){
        // Actualizacion de la informacion general
        FileConfiguration config = plugin.getConfig();

        generalPath = "Config.Drugs";
        generalDescription = config.getStringList(generalPath + ".General_Description");

        // Actualizacion de la configuracion de los items
        // PARAMS: Archivo conf, nombre para almacenar en los hashmaps, nombre en el archivo de config.yml
        updateDrugConfig(config, "Marihuana", "marihuana");
        updateDrugConfig(config, "Perico", "perico");
        updateDrugConfig(config, "LSD", "LSD");

        marihuana = createItem(Material.SWEET_BERRIES, ChatColor.DARK_GREEN + "Marihuana", "Marihuana");
        perico = createItem(Material.SUGAR, ChatColor.LIGHT_PURPLE + "Perico", "Perico");
        LSD = createItem(Material.PAPER, ChatColor.YELLOW + "LSD", "LSD");
    }
}
