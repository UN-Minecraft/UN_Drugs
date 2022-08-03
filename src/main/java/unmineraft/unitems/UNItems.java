package unmineraft.unitems;

import commands.DrugsCommand;
import events.DrugsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import unmineraft.unitems.consumable.Drugs;

public final class UNItems extends JavaPlugin {

    PluginDescriptionFile pdfile = getDescription();
    public String version = ChatColor.GREEN + pdfile.getVersion();
    public String name = ChatColor.YELLOW + "[" + ChatColor.GREEN + pdfile.getName() + ChatColor.YELLOW + "]";

    @Override
    public void onEnable() {
        String initPluginMessage = name + ChatColor.WHITE + " has been enabled in the version: " + version;
        Bukkit.getConsoleSender().sendMessage(initPluginMessage);

        Drugs.buildDrugs();

        commandRegister();
        eventsRegister();
    }

    @Override
    public void onDisable() {
        String endPluginMessage = name + ChatColor.RED + " has been disabled";
        Bukkit.getConsoleSender().sendMessage(endPluginMessage);
    }

    public void commandRegister(){
        this.getCommand("drug").setExecutor(new DrugsCommand(this));
    }

    public void eventsRegister(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DrugsEvent(), this);
    }
}
