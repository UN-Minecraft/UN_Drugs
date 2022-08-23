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

    /* Al tratarse de la administración del plugin, únicamente se es accesible desde la consola del servidor */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player) {
            String errorMessage = ChatColor.RED + " No puedes ejecutar este comando desde el cliente";
            sender.sendMessage(this.plugin.name + errorMessage);
            return false;
        } else if (args.length == 0){
            sender.sendMessage(plugin.name + ChatColor.DARK_PURPLE + "No se registra ninguna acción");
            return false;
        }

        /* El reinicio permite al plugin actualizar los cambios en la configuración a través de config.yml */
        if (args[0].equalsIgnoreCase("reload")){
            plugin.reloadConfig();
            sender.sendMessage(plugin.name + ChatColor.GREEN + "El plugin a sido recargado correctamente");

            /* Para que se efectúen los cambios es necesario volver a construir las drogas */
            Drugs.buildDrugs(plugin);

        } else {
            sender.sendMessage(plugin.name + ChatColor.RED + "El comando no ha sido reconocido");
            return false;
        }


        return true;
    }
}
