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

    private static final HashMap<UUID, String> lastDrugConsumed = new HashMap<>();

    private boolean isDifferentDrugEffectActive(Player player, String itemConsumed){
        // Si no ha consumido una droga antes
        if (!lastDrugConsumed.containsKey(player.getUniqueId())) return false;

        // El jugador no tiene efectos
        if (player.getActivePotionEffects().size() == 0) return false;

        // Si la droga es igual a la ultima consumida
        if (Objects.equals(lastDrugConsumed.get(player.getUniqueId()), itemConsumed)) return false;

        // Si el jugador tuvo una sobredosis
        if (Objects.equals(lastDrugConsumed.get(player.getUniqueId()), "Sobredosis")) {
            // Obtenemos la lista de tipos de efecto que tiene activos
            LinkedList<PotionEffectType> baseActiveEffectsTypes = new LinkedList<>();
            for (PotionEffect effect : player.getActivePotionEffects()){
                PotionEffectType typeEffect = effect.getType();

                // Validamos que no exista el tipo de efecto antes
                if (!baseActiveEffectsTypes.contains(typeEffect)){
                    baseActiveEffectsTypes.push(typeEffect);
                }
            }

            // Obtener los tipos de efecto de la sobredosis
            LinkedList<PotionEffectType> sobredosisEffects = new LinkedList<>();
            for (PotionEffect effect : Drugs.sobredosis){
                PotionEffectType typeEffect = effect.getType();

                // Validamos que no exista el tipo de efecto antes
                if (!sobredosisEffects.contains(typeEffect)){
                    sobredosisEffects.push(effect.getType());
                }
            }

            int coincidenceCounter = 0;

            // Buscamos coincidencias, si el número de coincidencias es igual al tamaño de los efectos de la sobredosis
            // asumimos que no ha terminado dicho efecto
            for (PotionEffectType typeEffect: baseActiveEffectsTypes){
                if (sobredosisEffects.contains(typeEffect)) coincidenceCounter++;
            }

            return (coincidenceCounter == sobredosisEffects.size());
        };

        // Si no ha tenido sobredosis y la droga es diferente a la última consumida
        LinkedList<PotionEffectType> baseActiveEffectsTypes = new LinkedList<>();
        for (PotionEffect effect : player.getActivePotionEffects()){
            PotionEffectType typeEffect = effect.getType();

            // Validamos que no exista el tipo de efecto antes
            if (!baseActiveEffectsTypes.contains(typeEffect)){
                baseActiveEffectsTypes.push(typeEffect);
            }
        }

        String nameLastState = lastDrugConsumed.get(player.getUniqueId());
        LinkedList<PotionEffectType> lastDrugConsumedEffects = new LinkedList<>();
        for (PotionEffect effect : (LinkedList<PotionEffect>) Drugs.drugsInformation.get(nameLastState).get("effects")){
            PotionEffectType typeEffect = effect.getType();

            // Validamos que no exista el tipo de efecto antes
            if (!baseActiveEffectsTypes.contains(typeEffect)){
                lastDrugConsumedEffects.push(typeEffect);
            }
        }

        int coincidenceCounter = 0;
        for (PotionEffectType typeEffect: baseActiveEffectsTypes){
            if (lastDrugConsumedEffects.contains(typeEffect)) coincidenceCounter++;
        }

        return (coincidenceCounter == lastDrugConsumedEffects.size());
    }


    /* La creación de objetos toma de base objetos ya existentes, por si el objeto base no es consumible
     * es necesaria la simulación de dicha característica a través de esta función */
    private void startEffects(Player player, ItemStack item){
        LinkedList<PotionEffect> itemEffects;
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

        boolean verifyActiveSobredosis = isDifferentDrugEffectActive(player, drugConsumed);
        if (!verifyActiveSobredosis){
            itemEffects = (LinkedList<PotionEffect>) Drugs.drugsInformation.get(drugConsumed).get("effects");
            player.addPotionEffects(itemEffects);
            lastDrugConsumed.put(player.getUniqueId(), drugConsumed);
        } else {
            // Se limpia cada efecto del jugador, y se implementan los efectos de la "sobredosis"
            for (PotionEffect effect : player.getActivePotionEffects()){
                player.removePotionEffect(effect.getType());
            }
            player.addPotionEffects(Drugs.sobredosis);
            lastDrugConsumed.put(player.getUniqueId(), "Sobredosis");
            player.sendMessage(DrugsEvent.plugin.name + ChatColor.RED + "Te ha dado una sobredosis, cuidado con mezclar sustancias");
        }
    }

    @EventHandler
    public void makeConsumable(PlayerInteractEvent event){
        if (event.getItem() != null){
            ItemStack item = event.getItem();

            /* En caso de que el objeto no este incluido en los items que tienen simulación de consumo
             * se ignora para que mantenga su comportamiento */
            if (!isConsumable.contains(item.getItemMeta())) return;
            Player player = event.getPlayer();
            UUID playerID = player.getUniqueId();

            if ((Action.RIGHT_CLICK_AIR.equals(event.getAction()) || Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) && !itemCooldown.contains(playerID)){
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1.0F, 1.0F); // Sonido: Acción de comer
                itemCooldown.add(player.getUniqueId()); // Evita el consumo de más de 1 unidad en menos de la ejecución de la animación

                /* El manejo de eventos es asíncrono, si queremos ejecutar código en el hilo principal
                *  es necesario emplear esta función */
                new BukkitRunnable(){
                    /* Al simular el consumo es necesario manejar el stack, por esta razón validamos que el jugador
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

                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F); // Sonido: Acción de eructar

                        String displayNameItem = Objects.requireNonNull(item.getItemMeta()).getDisplayName();
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

        // Listado de las drogas que tienen simulación de consumo
        isConsumable.add(Drugs.marihuana.getItemMeta());
        isConsumable.add(Drugs.perico.getItemMeta());
        isConsumable.add(Drugs.LSD.getItemMeta());
        isConsumable.add(Drugs.hongos.getItemMeta());
    }
}
