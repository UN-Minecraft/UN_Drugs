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

            // Si el jugador consume Marihuana
            if (Objects.equals(item.getItemMeta(), Drugs.marihuana.getItemMeta())){
                player.addPotionEffects(Drugs.effectsMap.get("Marihuana"));
            }

            // Si el jugador consume perico
            if (Objects.equals(item.getItemMeta(), Drugs.perico.getItemMeta())){
                player.addPotionEffects(Drugs.effectsMap.get("Perico"));
            }

            // Si el jugador consume LSD
            if (Objects.equals(item.getItemMeta(), Drugs.LSD.getItemMeta())){
                player.addPotionEffects(Drugs.effectsMap.get("LSD"));
            }
        }
    }
}
