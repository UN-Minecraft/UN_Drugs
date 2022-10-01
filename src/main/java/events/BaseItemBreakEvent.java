package events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import unmineraft.undrugs.UNDrugs;
import unmineraft.undrugs.items.craftBase.BaseItem;
import unmineraft.undrugs.utilities.GetterConfig;
import unmineraft.undrugs.utilities.MessagesConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BaseItemBreakEvent extends GetterConfig implements Listener {
    private final HashMap<Material, String> itemsCropBlocksMap = new HashMap<>();
    private final HashMap<Material, List<String>> blockCropDropItemsNameMap = new HashMap<>();

    public BaseItemBreakEvent(UNDrugs plugin){
        super(plugin);
        this.loadBaseItems();
    }

    private void loadBaseItemsTypeCrop(String sectionName){
        String path = "baseItems." + sectionName + ".obtainingInfo.blockMaterial";
        if (!super.checkExistence(path)) throw new IllegalArgumentException("ERROR_130: BLOCK MATERIAL PATH REFERENCE IS INVALID");

        String blockMaterialName = super.getPlainString(path);
        Material materialItem = Material.getMaterial(blockMaterialName);
        this.itemsCropBlocksMap.put(materialItem, sectionName);

        path = "baseItems." + sectionName + ".obtainingInfo.dropItems";
        List<String> dropItemsList = super.getStringList(path);
        this.blockCropDropItemsNameMap.put(materialItem, dropItemsList);
    }

    private void loadBaseItems(){
        if (!super.checkExistence("baseItems")) throw new IllegalArgumentException("ERROR_140: BASE ITEMS SECTION IS NULL");
        Set<String> sectionsName = super.getSections("baseItems");

        for (String sectionName : sectionsName){
            String path = "baseItems." + sectionName + ".obtainingInfo";
            if (!super.checkExistence(path + ".type")) continue;

            if (super.getPlainString(path + ".type").equals("crop")) {
                this.loadBaseItemsTypeCrop(sectionName);
            }
        }
    }

    private int getRandomAmount(String option){
        String[] rangeNum = option.split("-");
        int min = Integer.parseInt(rangeNum[0]), max = Integer.parseInt(rangeNum[1]);

        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }

    private void dropItems(Block block, Player player){
        Material blockType = block.getType();

        if (!this.blockCropDropItemsNameMap.containsKey(blockType)) return;
        for (String line : this.blockCropDropItemsNameMap.get(blockType)){
            ItemStack dropItem;
            String[] options = line.split(";");
            int itemStackNum = this.getRandomAmount(options[1]);

            // Base Item
            if (options[0].equals("self")){
                String sectionName = this.itemsCropBlocksMap.get(blockType);

                if (!BaseItem.baseItemMap.containsKey(sectionName)) throw new IllegalArgumentException("ERROR_150: BASE ITEM NON EXIST WITH THAT NAME");
                dropItem = BaseItem.baseItemMap.get(sectionName);
            }
            // Additional items
            else {
                Material itemMaterial = Material.getMaterial(options[0]);
                if (itemMaterial == null) continue;

                dropItem = new ItemStack(itemMaterial);
            }

            // Set the stack
            if (dropItem == null || dropItem.getItemMeta() == null) continue;
            dropItem.setAmount(itemStackNum);

            // Drop item in block location
            player.getWorld().dropItem(block.getLocation(), dropItem);

            // Send player message
            String message = MessagesConfig.getMessage("validCrop").replaceAll("%stackAmountDrop%", String.valueOf(itemStackNum)).replaceAll("%itemDisplayName%", dropItem.getItemMeta().getDisplayName());
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void getBaseItemCrop(BlockBreakEvent event){
        Block block = event.getBlock();
        Material blockType = block.getType();

        if (!this.itemsCropBlocksMap.containsKey(blockType)) return;

        Player player = event.getPlayer();
        BlockData blockData = block.getBlockData();
        Ageable ageable = (Ageable) blockData;

        if (ageable.getAge() < ageable.getMaximumAge()){
            player.sendMessage(MessagesConfig.getMessage("insufficientAgeCrop"));
            event.setCancelled(true);
            return;
        }

        event.setDropItems(false);
        this.dropItems(block, player);
    }

}
