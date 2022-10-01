package controllers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import unmineraft.undrugs.items.consumable.DrugItem;
import unmineraft.undrugs.utilities.MessagesConfig;
import unmineraft.undrugs.utilities.StrEnchant;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class ConsumeController {
    private static final HashMap<UUID, Date> lastConsumeDate = new HashMap<>();
    private static final HashMap<UUID, Date> lastOverdoseDate = new HashMap<>();
    private static final HashMap<UUID, ItemMeta> lastConsumedItem = new HashMap<>();

    public static Date getLastConsumedDate(Player player){
        UUID idPlayer = player.getUniqueId();
        if (!ConsumeController.lastConsumeDate.containsKey(idPlayer)) return null;

        return ConsumeController.lastConsumeDate.get(idPlayer);
    }

    public static void setLastConsumedDate(Player player, Date newDate){
        ConsumeController.lastConsumeDate.put(player.getUniqueId(), newDate);
    }

    public static Date getLastOverdoseDate(Player player){
        UUID idPlayer = player.getUniqueId();
        if (!ConsumeController.lastOverdoseDate.containsKey(idPlayer)) return null;

        return ConsumeController.lastOverdoseDate.get(idPlayer);
    }

    public static void setLastOverdoseDate(Player player, Date newDate){
        ConsumeController.lastOverdoseDate.put(player.getUniqueId(), newDate);
    }

    public static ItemMeta getLastConsumedItem(Player player){
        UUID idPlayer = player.getUniqueId();
        if (!ConsumeController.lastConsumedItem.containsKey(idPlayer)) return null;

        return ConsumeController.lastConsumedItem.get(idPlayer);
    }

    public static void setLastConsumedItem(Player player, ItemMeta itemMeta){
        ConsumeController.lastConsumedItem.put(player.getUniqueId(), itemMeta);
    }

    public static boolean checkActiveEffects(Date date, int durationEffects){
        Date actualDate = new Date(System.currentTimeMillis());
        long diff = date.getTime() - actualDate.getTime();

        long diffSeconds = diff / 1000 % 60;
        return diffSeconds <= durationEffects;
    }

    public static void applyEffects(Player player, ItemMeta itemConsumedMeta){
        LinkedList<PotionEffect> effects = DrugItem.itemEffectsMap.get(itemConsumedMeta);
        player.addPotionEffects(effects);
        Date actualDate = new Date(System.currentTimeMillis());

        String message = MessagesConfig.getMessage("consume");
        message = StrEnchant.replace(message, "%drugDisplayName%", itemConsumedMeta.getDisplayName());
        player.sendMessage(message);

        // Load important data
        ConsumeController.setLastConsumedDate(player, actualDate);
        ConsumeController.setLastConsumedItem(player, itemConsumedMeta);
    }
}
