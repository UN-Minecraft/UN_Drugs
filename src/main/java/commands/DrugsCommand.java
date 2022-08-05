package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.consumable.Drugs;

public class DrugsCommand implements CommandExecutor {
    private final UNDrugs plugin;

    public DrugsCommand(UNDrugs plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String errorMessage = ChatColor.RED + "No puedes ejecutar comandos desde la consola";
            sender.sendMessage(this.plugin.name + errorMessage);
            return false;
        }

        Player player = (Player) sender;
        String message = String.join(" ", args);
        ItemStack selectedItem;

        if (message.equalsIgnoreCase("marihuana")){
            selectedItem = Drugs.marihuana;
        } else if (message.equalsIgnoreCase("perico")){
            selectedItem = Drugs.perico;
        } else if (message.equalsIgnoreCase("lsd")){
            selectedItem = Drugs.LSD;
        } else {
            player.sendMessage(this.plugin.name + ChatColor.DARK_PURPLE + "Elemento no reconocido");
            return false;
        }
        player.getInventory().addItem(selectedItem);

        return true;
    }
}
