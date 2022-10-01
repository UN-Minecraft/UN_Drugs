package unmineraft.undrugs.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StrEnchant {
    public static String applyColors(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static String replace(String str, String patron, String value){
        return ChatColor.translateAlternateColorCodes('&', str.replaceAll(patron, value));
    }
}
