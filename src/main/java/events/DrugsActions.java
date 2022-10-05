package events;

import controllers.ConsumeController;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.consumable.DrugItem;
import unmineraft.undrugs.states.Overdose;
import unmineraft.undrugs.utilities.MessagesConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class DrugsActions implements Listener {
    private final UNDrugs plugin;
    private final HashMap<UUID, Long> cooldownAnimationConsume = new HashMap<>();

    public DrugsActions(UNDrugs plugin){
        this.plugin = plugin;
    }

    private boolean isOverdoseStateActive(Player player){
        // If the overdose is active, return true
        Date lastOverdoseDate = ConsumeController.getLastOverdoseDate(player);
        if (lastOverdoseDate != null) {
            return ConsumeController.checkActiveEffects(lastOverdoseDate, Overdose.effectsDuration, player);
        }
        return false;
    }

    private boolean isDrugActive(Player player){
        // If the player has not used drugs, return false
        Date lastConsumedDate = ConsumeController.getLastConsumedDate(player);
        if (lastConsumedDate == null) return false;

        Date actualDate = new Date(System.currentTimeMillis());
        ItemMeta lastConsumedItemMeta = ConsumeController.getLastConsumedItem(player);
        if (!DrugItem.itemEffectsDurationMap.containsKey(lastConsumedItemMeta)) throw new NullPointerException("ERROR_80: INVALID META");
        int durationEffects = DrugItem.itemEffectsDurationMap.get(lastConsumedItemMeta);

        /* If the record of the player's last use is more recent than the duration of the effects of the
         * last drug consumed, returns true */
        return ConsumeController.checkActiveEffects(actualDate, durationEffects, player);
    }

    private boolean isDifferentDrugActive(Player player, ItemStack drugConsumed){
        if (!this.isDrugActive(player)) return false;

        ItemMeta lastConsumedItemMeta = ConsumeController.getLastConsumedItem(player);
        ItemMeta drugConsumedMeta = drugConsumed.getItemMeta();

        if (lastConsumedItemMeta == null) throw new NullPointerException("ERROR_90: LAST DRUG CONSUMED META IS NULL");
        if (drugConsumedMeta == null) throw new NullPointerException("ERROR_100: DRUG CONSUMED META IS NULL");

        return !lastConsumedItemMeta.equals(drugConsumedMeta);
    }

    public void applyEffects(Player player, ItemStack drugConsumed){
        ItemMeta drugMeta = drugConsumed.getItemMeta();
        if (!DrugItem.itemEffectsMap.containsKey(drugMeta)) return;


        // If the player has no active effects, apply drug effects
        if (player.getActivePotionEffects().isEmpty()){
            ConsumeController.applyEffects(player, drugMeta);
            return;
        }

        // Check overdose state or different drug is consumed
        if (this.isOverdoseStateActive(player) || this.isDifferentDrugActive(player, drugConsumed)){
            Overdose.applyEffects(player);
            return;
        }

        // Otherwise
        ConsumeController.applyEffects(player, drugMeta);
    }

    @EventHandler
    public void simulateConsume(PlayerInteractEvent event){
        if (event.getItem() == null) return;
        ItemMeta itemMeta = event.getItem().getItemMeta();

        if (!DrugItem.itemMetaMap.containsValue(itemMeta)) return;

        Action action = event.getAction();

        if (!Action.RIGHT_CLICK_AIR.equals(action) && !Action.RIGHT_CLICK_BLOCK.equals(action)){
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        UUID idPlayer = player.getUniqueId();
        if (this.cooldownAnimationConsume.containsKey(idPlayer)){
            player.sendMessage(MessagesConfig.getMessage("cooldownConsume"));
            event.setCancelled(true);
            return;
        }

        // Simulate consume
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
        this.cooldownAnimationConsume.put(idPlayer, 20L);

        // Item and effects management
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            ItemStack item = event.getItem();
            if (player.getGameMode() != GameMode.CREATIVE) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                }

                if (item.getAmount() == 1){
                    player.getInventory().removeItem(item);
                    player.updateInventory();
                }
            }
            // Play a sound and apply effects
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F);
            this.applyEffects(player, item);
        }, 10L);

        // Clear id player from cooldown map
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.cooldownAnimationConsume.remove(idPlayer), 20L);
    }
}
