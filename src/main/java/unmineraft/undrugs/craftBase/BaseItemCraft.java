package unmineraft.undrugs.craftBase;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import unmineraft.undrugs.UNDrugs;

import java.util.*;

public class BaseItemCraft {
    private static final String generalPath = "Config.BaseItem";
    private static FileConfiguration config;
    public static String[] pathItems = {"Cogote"};

    /* Estructura de objetos que permite almacenar la información de cada elemento con base en nombres, luce algo asi:
            * itemInformation = {
     *    "Cogote" = {
                "DisplayName": Nombre en juego,
                "BaseItem": Referencia del Material,
                "Lore": Información adicional
     *    }
     *     ...
     * }
     */
    public static HashMap<String, HashMap<String, Object>> itemInformation = new HashMap<>();

    public static ItemStack cogote;

    /* Buscamos obtener una cadena no nula y la traducimos para aplicar sus colores y estilos */
    private static String getDisplayNameDrug(String path){
        try {
            String plainText = Objects.requireNonNull(config.getString(path));
            return ChatColor.translateAlternateColorCodes('&', plainText);
        } catch (Error error){
            return "";
        }
    }

    /* Obtenemos la configuración y generamos un item base sobre el cual se construye la droga */
    private static Material getBaseItem(String path){
        try {
            String MATERIAL_ITEM_NAME = Objects.requireNonNull(config.getString(path));

            return Material.getMaterial(MATERIAL_ITEM_NAME);
        } catch (Error error){
            return Material.ENDER_PEARL;
        }
    }

    /* Generamos la descripción base y aplicamos los efectos que genera su consumo */
    private static ArrayList<String> getLoreItem(String pathNameDrug){
        String path = generalPath + "." + pathNameDrug + ".Lore";

        try {
            ArrayList<String> baseLore = new ArrayList<>(config.getStringList(path));
            ArrayList<String> lore = new ArrayList<>();

            for (String line : baseLore){
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }

            return lore;
        } catch (Error error){
            return new ArrayList<>();
        }
    }

    private static void updateDrugConfig(String pathNameDrug){
        HashMap<String, Object> itemCraft = new HashMap<>();

        String path = generalPath + "." + pathNameDrug + ".BaseItem";
        itemCraft.put("baseItem", getBaseItem(path));

        path = generalPath + "." + pathNameDrug + ".DisplayName";
        itemCraft.put("displayName", getDisplayNameDrug(path));

        itemCraft.put("lore", getLoreItem(pathNameDrug));

        // Se guarda la información en la variable de clase
        itemInformation.put(pathNameDrug, itemCraft);
    }

    protected static ItemStack createItem(String pathNameItem){
        try {
            HashMap<String, Object> drugInfo = Objects.requireNonNull(itemInformation.get(pathNameItem));

            ItemStack item = new ItemStack((Material) drugInfo.get("baseItem"), 1);
            ItemMeta meta = item.getItemMeta();

            assert meta != null;
            meta.setDisplayName((String) Objects.requireNonNull(drugInfo.get("displayName")));

            meta.setLore((List<String>) Objects.requireNonNull(drugInfo.get("lore")));

            meta.addEnchant(Enchantment.LUCK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            item.setItemMeta(meta);
            return item;

        } catch (Error error){
            return new ItemStack(Material.SAND, 1);
        }
    }

    public static Material getItemMaterialByName(String name){
        String itemName = "";
        if (name.equalsIgnoreCase("Cogote")) {
            itemName = "Cogote";
        }

        if (itemName.equals("") || !(Arrays.toString(BaseItemCraft.pathItems).contains(itemName))){
            return Material.BEDROCK;
        }

        String path = generalPath + "." + name + ".BaseItem";

        if (getBaseItem(path) != null) return getBaseItem(path);
        return Material.SAND;
    }

    public static void buildItem(UNDrugs plugin){
        BaseItemCraft.config = plugin.getConfig();

        for (String pathNameDrug : BaseItemCraft.pathItems) {
            updateDrugConfig(pathNameDrug);
        }

        BaseItemCraft.cogote = createItem("Cogote");

    }
}
