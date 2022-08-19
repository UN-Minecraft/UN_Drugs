package events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.consumable.Drugs;

import java.util.*;

public class DrugsEvent implements Listener {
    private static UNDrugs plugin;

    private static final LinkedList<ItemMeta> isConsumable = new LinkedList<>();
    private static final LinkedList<UUID> itemCooldown = new LinkedList<>();

    private static HashMap<UUID, String> lastDrugConsumed = new HashMap<>();

    private boolean isDifferentDrugEffectActive(Player player, String itemConsumed, String lastConsumed){
        // Si no ha consumido una droga antes
        if (!lastDrugConsumed.containsKey(player.getUniqueId())) return false;

        // El jugador no tiene efectos
        if (player.getActivePotionEffects().size() == 0) return false;

        // Si la droga es igual a la ultima consumida
        if (lastDrugConsumed.get(player.getUniqueId()) == itemConsumed) return false;

        // Si no ha terminado el efecto de la sobredosis
        if (lastDrugConsumed.get(player.getUniqueId()) == "Sobredosis") return  true;

        // Si la droga es diferente a la ultima consumida, como aun tiene efectos activos entra en sobredosis
        LinkedList<PotionEffectType> baseActiveEffectsTypes = new LinkedList<>();
        for (PotionEffect effect : player.getActivePotionEffects()){
            baseActiveEffectsTypes.push(effect.getType());
        }

        LinkedList<PotionEffect> lastConsumedDrugEffects = (LinkedList<PotionEffect>) Drugs.drugsInformation.get(lastConsumed).get("effects");
        int coincidenceCounter = 0;

        for (PotionEffect effect : lastConsumedDrugEffects){
            if (baseActiveEffectsTypes.contains(effect.getType())) coincidenceCounter++;
        }

        if (coincidenceCounter >= lastConsumedDrugEffects.size()) return true;

        return false;
    }


    /* La creacion de objetos toma de base objetos ya existentes, por ende si el objeto base no es consumible
     * es necesaria la simulacion de dicha caracteristica a traves de esta funcion */
    private void startEffects(Player player, ItemStack item){
        LinkedList<PotionEffect> itemEffects = null;
        String drugConsumed = "";
        // Si el jugador consume Marihuana
        if (Objects.equals(item.getItemMeta(), Drugs.marihuana.getItemMeta())){
            drugConsumed = "Marihuana";
        }

        // Si el jugador consume Perico
        if (Objects.equals(item.getItemMeta(), Drugs.perico.getItemMeta())){
            drugConsumed = "Perico";
        }

        // Si el jugador consume LSD
        if (Objects.equals(item.getItemMeta(), Drugs.LSD.getItemMeta())){
            drugConsumed = "LSD";
        }

        // Si el jugador consume Hongos
        if (Objects.equals(item.getItemMeta(), Drugs.hongos.getItemMeta())) {
            drugConsumed = "Hongos";
        }

        Boolean verifyActiveSobredosis = isDifferentDrugEffectActive(player, drugConsumed, lastDrugConsumed.get(player.getUniqueId()));
        if (!verifyActiveSobredosis){
            itemEffects = (LinkedList<PotionEffect>) Drugs.drugsInformation.get(drugConsumed).get("effects");
            player.addPotionEffects(itemEffects);
            lastDrugConsumed.put(player.getUniqueId(), drugConsumed);
            return;
        }

        if (verifyActiveSobredosis){
            // Se limpia cada efecto del jugador, y se implementan los efectos de la "sobredosis"
            for (PotionEffect effect : player.getActivePotionEffects()){
                player.removePotionEffect(effect.getType());
            }
            player.addPotionEffects(Drugs.sobredosis);
            lastDrugConsumed.put(player.getUniqueId(), "Sobredosis");
            player.sendMessage(DrugsEvent.plugin.name + ChatColor.RED + "Cuidado con mezclar sustancias");
        }
    }

    @EventHandler
    public void makeConsumable(PlayerInteractEvent event){
        if (event.getItem() != null){
            ItemStack item = event.getItem();

            /* En caso de que el objeto no este incluido en los items que tienen simulacion de consumo
             * se ignora para que mantenga su comportamiento */
            if (!isConsumable.contains(item.getItemMeta())) return;
            Player player = event.getPlayer();
            UUID playerID = player.getUniqueId();

            if ((Action.RIGHT_CLICK_AIR.equals(event.getAction()) || Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) && !itemCooldown.contains(playerID)){
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F); // Sonido: Accion de comer
                itemCooldown.add(player.getUniqueId()); // Evita el consumo de mas de 1 unidad en menos de la ejecucion de la animacion

                /* El manejo de eventos es asincrono, si queremos ejecutar codigo en el hilo principal
                *  es necesario emplear esta funcion */
                new BukkitRunnable(){
                    /* Al simular el consumo es necesario manejar el stack, por esta razon validamos que el jugador
                    *  se encuentre en el modo survival, en dicho caso al ser consumido se reduce el stack en 1 unidad */
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

                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F); // Sonido: Accion de eructar

                        String displayNameItem = "Objeto desconocido";

                        if (item.getItemMeta().getDisplayName() != null) {
                            displayNameItem = item.getItemMeta().getDisplayName();
                        }


                        player.sendMessage(DrugsEvent.plugin.name + ChatColor.WHITE + "Has consumido: " + displayNameItem);

                        startEffects(player, item); // Inicio de los efectos de la droga
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

        // Listado de las drogas que tienen simulacion de consumo
        isConsumable.add(Drugs.marihuana.getItemMeta());
        isConsumable.add(Drugs.perico.getItemMeta());
        isConsumable.add(Drugs.LSD.getItemMeta());
        isConsumable.add(Drugs.hongos.getItemMeta());
    }
}
