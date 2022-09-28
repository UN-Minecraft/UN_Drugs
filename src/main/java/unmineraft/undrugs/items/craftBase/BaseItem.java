package unmineraft.undrugs.items.craftBase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import unmineraft.undrugs.UNDrugs;

import java.util.*;

public class BaseMaterial {
    private static FileConfiguration fileConfiguration;

    public static HashMap<String, Object> itemData = new HashMap<>();
    public static HashMap<String, ItemStack> baseItemReference = new HashMap<>();

    public static void buildItems(UNDrugs plugin){
        return;
    }
}
