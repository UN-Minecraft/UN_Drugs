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
                    ItemStack item = mapBlockDropItem.get(block.getType());

                    int randomNum = getRandomNumber(1, 4);
                    item.setAmount(randomNum);

                    // Drop del cogote
                    player.getWorld().dropItem(player.getLocation(), item);
                    player.sendMessage(plugin.name + ChatColor.WHITE + "Haz recolectado " + randomNum + " nuevas unidades");

                    // Drop de semillas
                    ItemStack seeds = new ItemStack(Material.BEETROOT_SEEDS, getRandomNumber(0, 2));
                    player.getWorld().dropItem(player.getLocation(), seeds);
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
