package unmineraft.undrugs.utilities;

import org.bukkit.ChatColor;

public class StrEnchant {
    public static String applyColors(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static String replace(String str, String patron, String value){
        return ChatColor.translateAlternateColorCodes('&', str.replace(patron, value));
    }
}
