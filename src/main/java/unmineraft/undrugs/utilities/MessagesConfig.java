package unmineraft.undrugs.utilities;

import org.bukkit.configuration.ConfigurationSection;
import unmineraft.undrugs.UNDrugs;

import java.util.HashMap;

public class MessagesConfig extends GetterConfig{
    private static final HashMap<String, String> messages = new HashMap<>();

    public static String getMessage(String sectionName){
        if (!MessagesConfig.messages.containsKey(sectionName)) return "";
        return MessagesConfig.messages.get(sectionName);
    }

    public MessagesConfig(UNDrugs plugin){
        super(plugin);
    }

    public void loadMessages(){
        if (!super.checkExistence("messages")) throw new IllegalArgumentException("ERROR_110: MESSAGES CONFIG NOT LOAD");

        ConfigurationSection auxConfigurationSection = this.plugin.getConfig().getConfigurationSection("messages");
        if (auxConfigurationSection == null) throw new NullPointerException("ERROR_120: CONFIGURATION SECTION IS NULL");

        for (String sectionName : auxConfigurationSection.getKeys(false)){
            String message = this.plugin.name + super.getFormatString("messages." + sectionName);
            MessagesConfig.messages.put(sectionName, message);
        }
    }
}
