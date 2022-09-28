package unmineraft.undrugs;

import commands.BaseItemCraftCommand;
import commands.DrugsCommand;
import commands.UnDrugsCommands;
import events.BaseItemBreakEvent;
import events.CraftDrugEvent;
import events.DrugsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import unmineraft.undrugs.consumable.Drugs;
import unmineraft.undrugs.craftBase.BaseItemCraft;
import unmineraft.undrugs.utilities.StrEnchant;

import java.io.File;
import java.util.Objects;

public final class UNDrugs extends JavaPlugin {
    public String pathConfig;

    PluginDescriptionFile pdfile = this.getDescription();
    public String version;
    public String name;

    private void updateMetaData(FileConfiguration fileConfiguration){
        String baseAttribute = this.pdfile.getName();

        String baseString = fileConfiguration.getString("config.pluginDisplayName");
        if (baseString == null) this.name = baseAttribute;
        this.name = StrEnchant.replace(baseString, "%pluginName%", baseAttribute);

        this.version = StrEnchant.applyColors("&a" + this.pdfile.getVersion());
    }

    public void commandRegister(){
        Objects.requireNonNull(this.getCommand("drugs")).setExecutor(new DrugsCommand(this));
        Objects.requireNonNull(this.getCommand("baseDrug")).setExecutor(new BaseItemCraftCommand(this));
        Objects.requireNonNull(this.getCommand("undrugs")).setExecutor(new UnDrugsCommands(this));
    }

    public void eventsRegister(){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DrugsEvent(this), this);
        pluginManager.registerEvents(new BaseItemBreakEvent(this), this);
        pluginManager.registerEvents(new CraftDrugEvent(this), this);
    }

    public void configRegister(){
        File config = new File(this.getDataFolder(), "config.yml");
        pathConfig = config.getPath();

        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.updateMetaData(this.getConfig());

        String initPluginMessage = StrEnchant.applyColors(name + "&f has been enabled in the version: " + this.version);
        Bukkit.getConsoleSender().sendMessage(initPluginMessage);

        BaseItemCraft.buildItem(this);

        Drugs.buildDrugs(this);

        commandRegister();
        eventsRegister();
        configRegister();
    }

    @Override
    public void onDisable() {
        String endPluginMessage = StrEnchant.applyColors(name + "&c has been disabled");
        Bukkit.getConsoleSender().sendMessage(endPluginMessage);
    }
}
