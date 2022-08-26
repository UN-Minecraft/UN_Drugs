package events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
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

            Player player = event.getPlayer();

            // Cancelamos el dropeo de items
            event.setDropItems(false);

            /* Ejecución de la entrega del item sobre el hilo principal */
            new BukkitRunnable(){
                @Override
                public void run(){
                    int indexFirstEmpty = player.getInventory().firstEmpty();
                    if (indexFirstEmpty == -1){
                        player.sendMessage(plugin.name + ChatColor.WHITE + "Inventario Lleno");
                        return;
                    }

                    if (mapBlockDropItem.containsKey(block.getType())){
                        ItemStack item = mapBlockDropItem.get(block.getType());

                        item.setAmount(getRandomNumber(1, 3));
                        player.getInventory().setItem(indexFirstEmpty, item);
                        player.sendMessage(plugin.name + ChatColor.WHITE + "Con esto puedes crear tus propias drogas");
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
