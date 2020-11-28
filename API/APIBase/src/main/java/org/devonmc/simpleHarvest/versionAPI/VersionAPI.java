package org.devonmc.simpleHarvest.versionAPI;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface VersionAPI {

    // Check if the block object passed is harvestable
    boolean isHarvestable(Block block);

    // Check if the block object passed is ripe
    boolean isRipe(Block block);

    // return a list of Itemstack drops based on the player object and the block object passed
    List<ItemStack> getDrops(Player player, Block block);

    // return the itemstack seed based on the material object passed
    ItemStack getSeed(Material material);

    // resets the age of the block object passed
    void resetAge(Block block);

}
