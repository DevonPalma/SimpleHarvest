package io.github.DevonPalma.simpleHarvest;

import io.github.DevonPalma.simpleHarvest.harvesters.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HarvestListener implements Listener {

    private final List<SimpleHarvester> harvesters;

    public HarvestListener() {
        this.harvesters = new ArrayList<>();
        this.harvesters.add(new WheatHarvester());
        this.harvesters.add(new BeetRootHarvester());
        this.harvesters.add(new CarrotHarvester());
        this.harvesters.add(new PotatoHarvester());
        this.harvesters.add(new NetherwartHarvester());
        this.harvesters.add(new CocoaHarvester());
    }

    public int getFortuneLevel(ItemStack item) {
        if (item == null)
            return 0;
        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
    }


    @EventHandler
    public void blockInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        int fortuneLevel = getFortuneLevel(e.getItem());

        for (SimpleHarvester harvester : harvesters) {
            boolean success = harvester.attemptHarvest(player, block, fortuneLevel);
            if (success) {
                e.setCancelled(true);
                break;
            }
        }
    }
}
