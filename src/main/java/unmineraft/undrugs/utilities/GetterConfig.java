package unmineraft.undrugs.utilities;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import unmineraft.undrugs.UNDrugs;

import java.util.List;
import java.util.Set;

public class GetterConfig {
    private final FileConfiguration fileConfiguration;
    protected UNDrugs plugin;

    public GetterConfig(UNDrugs plugin){
        this.plugin = plugin;
        this.fileConfiguration = plugin.getConfig();
    }

    protected boolean checkExistence(String path){
        return this.fileConfiguration.contains(path);
    }

    protected List<String> getStringList(String path){
        return this.fileConfiguration.getStringList(path);
    }

    protected String getPlainString(String path){
        String baseText = this.fileConfiguration.getString(path);
        if (baseText == null) return "";

        return baseText.trim();
    }

    protected String getFormatString(String path){
        String baseText = this.getPlainString(path);
        return StrEnchant.applyColors(baseText);
    }

    protected int getInt(String path){
        return this.fileConfiguration.getInt(path);
    }

    protected Material getMaterial(String path){
        String materialName = this.getPlainString(path);
        Material baseMaterial = Material.getMaterial(materialName);

        if (baseMaterial == null){
            throw new NullPointerException("ERROR_10: UNKNOWN MATERIAL");
        }
        return baseMaterial;
    }

    protected Set<String> getSections(String path){
        ConfigurationSection configurationSection = this.fileConfiguration.getConfigurationSection(path);
        if (configurationSection == null) return null;
        return configurationSection.getKeys(false);
    }
}
