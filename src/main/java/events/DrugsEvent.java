package events;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.consumable.Drugs;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

public class DrugsEvent implements Listener {
    private static UNDrugs plugin;

    private static final LinkedList<ItemMeta> isConsumable = new LinkedList<>();
    private static final LinkedList<UUID> itemCooldown = new LinkedList<>();


    private void startEffects(Player player, ItemStack item){
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

    @EventHandler
    public void makeConsumable(PlayerInteractEvent event){
        if (event.getItem() != null){
            ItemStack item = event.getItem();

            if (!isConsumable.contains(item.getItemMeta())) return;
            Player player = event.getPlayer();
            UUID playerID = player.getUniqueId();
            if ((Action.RIGHT_CLICK_AIR.equals(event.getAction()) || Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) && !itemCooldown.contains(playerID)){
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
                itemCooldown.add(player.getUniqueId());

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if (player.getGameMode() == GameMode.SURVIVAL){
                            if (item.getAmount() == 1){
                                player.getInventory().removeItem(item);
                                player.updateInventory();
                                event.setCancelled(true);
                            } else {
                                item.setAmount(item.getAmount() - 1);
                            }
                        }

                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F);

                        startEffects(player, item);
                        new BukkitRunnable(){
                            @Override
                            public void run(){
                                itemCooldown.remove(playerID);
                            }
                        }.runTaskLater(plugin, 10);
                    }
                }.runTaskLater(plugin, 20);
            }
        }
    }

    public DrugsEvent(UNDrugs plugin){
        DrugsEvent.plugin = plugin;

        isConsumable.add(Drugs.marihuana.getItemMeta());
        isConsumable.add(Drugs.perico.getItemMeta());
        isConsumable.add(Drugs.LSD.getItemMeta());
    }
}
