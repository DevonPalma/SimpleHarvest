package io.github.DevonPalma.simpleHarvest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityListener implements Listener {

    @EventHandler
    public void onEntityInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if (!SimpleHarvest.getVersionAPI().isHarvestable(block))
            return;


        if (!SimpleHarvest.getVersionAPI().isRipe(block))
            return;

        ItemStack seed = SimpleHarvest.getVersionAPI().getSeed(block.getType());
        List<ItemStack> drops = SimpleHarvest.getVersionAPI().getDrops(player, block);

        boolean seedFound = false;

        for (ItemStack i : drops) {
            if (i.getType() == seed.getType()) {
                int newAmount = i.getAmount() - 1;
                if (newAmount > 0)
                    i.setAmount(newAmount);
                else
                    drops.remove(i);
                seedFound = true;
                break;
            }
        }

        if (!seedFound) {
            Inventory inventory = player.getInventory();
            if (inventory.contains(seed, 1)) {
                inventory.removeItem(seed);
                seedFound = true;
            }
        }

        if (seedFound)
            SimpleHarvest.getVersionAPI().resetAge(block);
        else
            block.setType(Material.AIR);

        for (ItemStack i : drops)
            block.getWorld().dropItem(block.getLocation(), i);
    }
}
