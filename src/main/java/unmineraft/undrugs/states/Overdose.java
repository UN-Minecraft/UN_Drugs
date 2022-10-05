package unmineraft.undrugs.states;

import controllers.ConsumeController;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.builder.ItemBuilder;
import unmineraft.undrugs.utilities.MessagesConfig;

import java.util.Date;
import java.util.LinkedList;

public class Overdose extends ItemBuilder {
    public static LinkedList<PotionEffect> effects = new LinkedList<>();
    public static int effectsDuration;

    public static void applyEffects(Player player) {
        // Clean effects
        for (PotionEffect effect : player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffects(Overdose.effects);
        player.sendMessage(MessagesConfig.getMessage("overdose"));

        // Load last overdose status of player
        Date actualDate = new Date(System.currentTimeMillis());
        ConsumeController.setLastOverdoseDate(player, actualDate);
    }

    public Overdose(UNDrugs plugin) {
        super(plugin);
    }

    public void loadEffects(){
        Overdose.effects = super.getEffects("states.overdose");
    }

    public void loadDurationEffects(){
        Overdose.effectsDuration = super.getEffectsDuration("states.overdose");
    }
}
