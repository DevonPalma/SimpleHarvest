package io.github.DevonPalma.simpleHarvest;

import lombok.*;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

@Builder
public class CropHarvester {

    @Getter @Setter @NonNull
    private Material blockType, seedType;
    @Getter @Setter @Builder.Default
    private Sound breakSound = Sound.BLOCK_CROP_BREAK;
    @Getter @Setter
    private String permission;



    private boolean canHarvest(Player player, Block block) {
        // Check if the block to be harvested is of the corresponding type
        if (block.getType() != blockType)
            return false;

        // Check if player has permission to use this harvester
        if (!SimpleHarvest.getPerms().has(player, getPermission()))
            return false;

        if (!CropUtils.isRipe(block))
            return false;

        return true;
    }




    public boolean isCreative(Player player) {
        return player.getGameMode() == GameMode.CREATIVE;
    }




    public boolean hasSeed(Collection<ItemStack> drops) {
        for (ItemStack i : drops) {
            if (i.getType() == seedType && i.getAmount() > 0)
                return true;
        }
        return false;
    }

    public boolean hasSeed(Player player) {
        Inventory inventory = player.getInventory();
        return inventory.contains(seedType, 1);
    }


    public void removeSeed(Collection<ItemStack> drops) {
        for (ItemStack i : drops) {
            if (i.getType() == seedType) {
                int newCount = i.getAmount() - 1;
                if (newCount == 0)
                    drops.remove(i);
                else
                    i.setAmount(newCount);
                break;
            }
        }
    }

    public void removeSeed(Player player) {
        Inventory inventory = player.getInventory();
        inventory.removeItem(new ItemStack(seedType, 1));
    }



    public void playBlockBreakSound(Player player, Block block) {
        block.getWorld().playSound(block.getLocation(), breakSound, SoundCategory.BLOCKS, 1, 0.9F);
    }



    public boolean tryHarvest(PlayerInteractEvent e)  {
        Block block = e.getClickedBlock();
        Player player = e.getPlayer();

        if (!canHarvest(player, block))
            return false;

        Collection<ItemStack> drops = block.getDrops(e.getItem(), player);


        boolean breakBlock = true;
        if (isCreative(player)) {
            breakBlock = false;
        }
        else if (hasSeed(drops)) {
            removeSeed(drops);
            breakBlock = false;
        }
        else if (hasSeed(player)) {
            removeSeed(player);
            breakBlock = false;
        }

        if (breakBlock)
            block.setType(Material.AIR);
        else
            CropUtils.setBlockAge(block, 0);

        playBlockBreakSound(player, block);

        for (ItemStack drop : drops)
            block.getWorld().dropItem(block.getLocation(), drop);

        return true;
    }
}
