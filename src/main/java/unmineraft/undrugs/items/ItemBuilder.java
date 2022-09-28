package unmineraft.undrugs.items;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.utilities.StrEnchant;

import java.util.*;

public class ItemBuilder {
    private FileConfiguration fileConfiguration;

    public ItemBuilder(UNDrugs plugin){
        this.fileConfiguration = plugin.getConfig();
    }

    protected List<String> getStringList(String path){
        List<String> stringList = this.fileConfiguration.getStringList(path);
        if (stringList == null) return Collections.singletonList("");
        return stringList;
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

    public List<String> getGeneralDescription() {
        return this.getStringList("config.generalDescription");
    }

    public int getEffectsDuration(String pathDrug){
        int TICKS_PER_SECOND = 20, durationValue = this.getInt(pathDrug + ".durationInSeconds");
        return TICKS_PER_SECOND * durationValue;
    }

    public HashMap<PotionEffectType, Integer> getEffectsType(String pathDrug){
        HashMap<PotionEffectType, Integer> effectTypeLevelMap = new HashMap<>();

        for (String effectType : this.getStringList(pathDrug + ".effects")){
            String[] effectInfo = effectType.trim().split(";");

            String effectTypeName = effectInfo[0].trim();

            int effectTypeLevel = 0;
            try {
                effectTypeLevel = Integer.parseInt(effectInfo[1]) - 1;
            } catch (Exception exception){
                exception.printStackTrace();
                continue;
            }

            PotionEffectType potionEffectType = PotionEffectType.getByName(effectTypeName);
            if (potionEffectType == null) continue;

            effectTypeLevelMap.put(potionEffectType, effectTypeLevel);
        }
        return effectTypeLevelMap;
    }

    public LinkedList<PotionEffect> getEffects(String pathDrug){
        LinkedList<PotionEffect> itemEffects = new LinkedList<>();
        HashMap<PotionEffectType, Integer> itemDataMap = this.getEffectsType(pathDrug);
        int durationEffect = this.getEffectsDuration(pathDrug);

        for (PotionEffectType effectType : itemDataMap.keySet()){
            int effectTypeLevel = itemDataMap.get(effectType);
            PotionEffect effect = new PotionEffect(effectType, durationEffect, effectTypeLevel);
            itemEffects.push(effect);
        }
        return itemEffects;
    }

    public String getDisplayName(String pathDrug){
        return this.getFormatString(pathDrug + ".displayName");
    }

    public Material getBaseMaterial(String pathDrug){
        return this.getMaterial(pathDrug + ".baseMaterial");
    }

    public List<String> getAdditionalLore(String pathDrug){
        return this.getStringList(pathDrug + ".additionalLore");
    }

    public String getLabelEffects(String pathDrug){
        return this.getFormatString(pathDrug + ".labelEffects");
    }

    public ArrayList<String> buildLore(String pathDrug){
        String labelEffects = this.getLabelEffects(pathDrug);

        ArrayList<String> itemLore = new ArrayList<>();
        for (String line : this.getGeneralDescription()){
            String finalLine = StrEnchant.replace(line, "%effectsDrug%", labelEffects);
            itemLore.add(finalLine);
        }
        itemLore.addAll(this.getAdditionalLore(pathDrug));

        return itemLore;
    }

    public ItemStack buildItem() {
        return null;
    }

    public void storeItem(){
        return;
    }
}
