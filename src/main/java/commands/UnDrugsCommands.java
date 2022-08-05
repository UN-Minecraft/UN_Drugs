package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.consumable.Drugs;

public class UnDrugsCommands implements CommandExecutor {
    private final UNDrugs plugin;

    public UnDrugsCommands(UNDrugs plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player) {
            String errorMessage = ChatColor.RED + " No puedes ejecutar este comando desde el cliente";
            sender.sendMessage(this.plugin.name + errorMessage);
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")){
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "El plugin a sido recargado correctamente");

            Drugs.buildDrugs(plugin);

        } else {
            sender.sendMessage(ChatColor.RED + "El comando no ha sido reconocido");
            return false;
        }


        return true;
    }
}