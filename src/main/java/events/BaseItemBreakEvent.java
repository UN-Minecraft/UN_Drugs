package events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.craftBase.BaseItemCraft;

import java.util.HashMap;
import java.util.LinkedList;

public class BaseItemBreakEvent implements Listener {
    private static UNDrugs plugin;

    private static final LinkedList<Material> isBaseBlockItem = new LinkedList<>();
    private static final HashMap<Material, ItemStack> mapBlockDropItem = new HashMap<>();

    private static int getRandomNumber(int min, int max){
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }

    @EventHandler
    public void getBaseItem(BlockBreakEvent event){
        if (event.getBlock() != null && event.getPlayer() != null){
            Block block = event.getBlock();

            // Si no es un bloque que necesitemos manejar, finaliza la ejecución
            if (!(isBaseBlockItem.contains(block.getType()))) return;


            BlockData dataBlock = block.getBlockData();
            Ageable ageable = (Ageable) dataBlock;

            if (ageable.getAge() < ageable.getMaximumAge()) {
                event.getPlayer().sendMessage(plugin.name + ChatColor.WHITE + "Aun no ha crecido lo suficiente");
                event.setCancelled(true);
                return;
            }

            Player player = event.getPlayer();

            // Cancelamos el dropeo de items
            event.setDropItems(false);

            /* Ejecución de la entrega del item sobre el hilo principal */
            new BukkitRunnable(){
                @Override
                public void run(){
                    int indexFirstEmpty = player.getInventory().firstEmpty();

                    ItemStack item = mapBlockDropItem.get(block.getType());
                    PlayerInventory playerInventory = player.getInventory();

                    // Si no hay espacio ni un stack disponible
                    if (indexFirstEmpty == -1 && !(playerInventory.contains(item.getType()))){
                        player.sendMessage(plugin.name + ChatColor.WHITE + "Inventario Lleno");
                        return;
                    }

                    int randomNum = getRandomNumber(1, 4);
                    if (mapBlockDropItem.containsKey(block.getType())){
                        // Si el jugador ya tiene el item, sumamos al stack los que acaba de "cultivar"
                        if (playerInventory.contains(item.getType())){
                            int slot = playerInventory.first(item.getType());
                            ItemStack savedItem = playerInventory.getItem(slot);

                            if (savedItem == null) return;

                            // Obtenemos el tamaño del stack
                            int stack = savedItem.getAmount();

                            // Si el stack está lleno creamos otro
                            if (stack == 64){
                                item.setAmount(randomNum);
                                System.out.println(randomNum);

                                // Si no tiene espacio disponible en el stack y no tiene mas espacio no se entrega el item
                                if (indexFirstEmpty == -1){
                                    player.sendMessage(plugin.name + ChatColor.WHITE + "Inventario Lleno");
                                    return;
                                }
                                playerInventory.setItem(indexFirstEmpty, item);
                            } else {
                                savedItem.setAmount(stack + randomNum);
                                playerInventory.setItem(slot, savedItem);
                            }

                        } else {
                            item.setAmount(randomNum);
                            player.getInventory().setItem(indexFirstEmpty, item);
                        }
                        player.sendMessage(plugin.name + ChatColor.WHITE + "Haz recolectado " + randomNum + " nuevas unidades");
                    }
                }
            }.run();
        }
    }

    public BaseItemBreakEvent(UNDrugs plugin){
        BaseItemBreakEvent.plugin = plugin;

        // Lista de bloques a analizar
        isBaseBlockItem.push(Material.BEETROOTS);

        // Items a entregar
        mapBlockDropItem.put(Material.BEETROOTS, BaseItemCraft.cogote);
    }
}
