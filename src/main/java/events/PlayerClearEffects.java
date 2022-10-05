package events;

import controllers.ConsumeController;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerClearEffects implements Listener {
    @EventHandler
    public void playerConsumeMilk(PlayerItemConsumeEvent event){
        if (event.getItem().getType() == Material.MILK_BUCKET){
            ConsumeController.clearRegisters(event.getPlayer());
        }
    }
}
