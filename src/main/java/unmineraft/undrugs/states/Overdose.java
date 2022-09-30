package unmineraft.undrugs.states;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.builder.ItemBuilder;

import java.util.LinkedList;

public class Overdose extends ItemBuilder {
    public static LinkedList<PotionEffect> effects = new LinkedList<>();

    public static void applyEffects(Player player){
        player.addPotionEffects(Overdose.effects);
    }

    public Overdose(UNDrugs plugin){
        super(plugin);
        Overdose.effects = super.getEffects("states.overdose");
    }



    // TODO: Verify Conditions To State
}
