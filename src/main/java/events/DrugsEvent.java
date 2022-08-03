package events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import unmineraft.unitems.consumable.Drugs;

import java.util.LinkedList;
import java.util.Objects;

public class DrugsEvent implements Listener {
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){
        if (event.getItem() != null){
            ItemStack item = event.getItem();
            Player player = event.getPlayer();
            LinkedList<PotionEffect> effects = new LinkedList<>();

            if (Objects.equals(item.getItemMeta(), Drugs.marihuana.getItemMeta())){
                PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER, Drugs.DURATION_EFFECT_IN_SECONDS, 5);
                PotionEffect regeneration = new PotionEffect(PotionEffectType.REGENERATION, Drugs.DURATION_EFFECT_IN_SECONDS, 4);

                effects.push(hunger);
                effects.push(regeneration);

                player.addPotionEffects(effects);
            }
        }
    }
}
