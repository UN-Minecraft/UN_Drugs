package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.craftBase.BaseItem;

public class BaseItemCraftCommand implements CommandExecutor {
    private final UNDrugs plugin;

    public BaseItemCraftCommand(UNDrugs plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        /*
         * Se verifica que el emisor del comando sea un jugador, esto debido a que para la asignación de una droga
         * es necesario que el usuario que lo solicita permita el manejo del inventario. En caso tal de que se realize
         * desde consola, este enviara un mensaje de error
         */
        if (!(sender instanceof Player)) {
            String errorMessage = ChatColor.RED + "No puedes ejecutar comandos desde la consola";
            sender.sendMessage(this.plugin.name + errorMessage);
            return false;
        }

        /*
         * Se crea el jugador realizando la conversion de CommandSender a Player.
         * Los argumentos del comando se unen y sé general mensaje registrado, posteriormente
         * se verifica si alguno de los elementos existentes corresponde con el solicitado y en caso tal
         * se le adicionara al jugador el item.
         */
        Player player = (Player) sender;
        String message = String.join(" ", args);
        ItemStack selectedItem;

        if (message.equalsIgnoreCase("cogote")){
            selectedItem = BaseItem.baseItemMap.get("cogote");
        } else {
            player.sendMessage(this.plugin.name + ChatColor.DARK_PURPLE + "Elemento solicitado no reconocido");
            return false;
        }

        int indexFirstEmpty = player.getInventory().firstEmpty();
        if (indexFirstEmpty == -1) {
            player.sendMessage(this.plugin.name + ChatColor.RED + "Inventario lleno");
            return false;
        }

        selectedItem.setAmount(64);
        player.getInventory().setItem(indexFirstEmpty, selectedItem);
        player.sendMessage(this.plugin.name + ChatColor.WHITE + "Al parecer eres mas de hacer las cosas por ti mismo, llámame si necesitas algo");
        return true;
    }
}
