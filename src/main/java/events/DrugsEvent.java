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

            // Si el jugador consume Marihuana
            if (Objects.equals(item.getItemMeta(), Drugs.marihuana.getItemMeta())){
                Integer duration = Drugs.effectsDuration.get("Marihuana");

                PotionEffect hunger = new PotionEffect(PotionEffectType.HUNGER, duration, 0);
                PotionEffect regeneration = new PotionEffect(PotionEffectType.REGENERATION, duration, 1);

                effects.push(hunger);
                effects.push(regeneration);

                player.addPotionEffects(effects);
            }

            // Si el jugador consume perico

            // Si el jugador consume LSD
        }
    }
}
