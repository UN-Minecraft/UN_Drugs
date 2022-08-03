package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import unmineraft.unitems.UNItems;
import unmineraft.unitems.consumable.Drugs;

public class DrugsCommand implements CommandExecutor {
    private final UNItems plugin;

    public DrugsCommand(UNItems plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String errorMessage = ChatColor.RED + " No puedes ejecutar comandos desde la consola";
            sender.sendMessage(this.plugin.name + errorMessage);
        }

        Player player = (Player) sender;
        String message = String.join(" ", args);

        if (message.equalsIgnoreCase("marihuana")){
            player.getInventory().addItem(Drugs.marihuana);
        } else {
            player.sendMessage(this.plugin.name + ChatColor.DARK_PURPLE + "Elemento no reconocido");
        }

        return true;
    }
}
