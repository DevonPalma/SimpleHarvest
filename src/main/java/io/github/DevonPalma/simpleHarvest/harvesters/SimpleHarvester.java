package io.github.DevonPalma.simpleHarvest.harvesters;

import io.github.DevonPalma.simpleHarvest.SimpleHarvest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class SimpleHarvester {

    private final Material blockType;
    private String permission;


    public SimpleHarvester(Material blockType) {
        this.blockType = blockType;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Checks to see if the block type matches the harvester's
     * type
     * @param block the block to be checked
     * @return is harvestable by this harvester
     */
    public boolean isHarvestable(Block block) {
        return block.getType() == blockType;
    }

    /** checks to see if a block is ripe
     * uses the Ageable interface to check for ripeness
     * if the Ageable interface isn't present, then returns
     * true, otherwise checks to see if the current age, is at
     * the max age
     *
     * @param block The block to be checked
     * @return if the block is ripe
     */
    public boolean isRipe(Block block) {
        BlockData blockData = block.getBlockData();
        if (!(blockData instanceof Ageable)) {
            return true;
        }
        Ageable ageable = (Ageable) blockData;
        return ageable.getAge() == ageable.getMaximumAge();
    }

    /** Generates a list of drops based on the fortune level being used
     *
     * @param fortune the level of fortune on the tool being used
     * @return A list of drops
     */
    public abstract List<ItemStack> generateDrops(int fortune);


    public void dropItems(Block block, List<ItemStack> drops) {
        World world = block.getWorld();
        Location location = block.getLocation();

        for (ItemStack drop : drops) {
            if (drop == null)
                continue;
            world.dropItem(location, drop);
        }
    }

    public void resetBlockAge(Block block) {
        BlockData blockData = block.getBlockData();
        Ageable ageable = (Ageable) blockData;
        ageable.setAge(0);
        block.setBlockData(ageable);
    }


    public boolean attemptHarvest(Player player, Block block, int fortune) {
        if (!isHarvestable(block))
            return false;

        if (!SimpleHarvest.getPerms().has(player, permission))
            return false;

        if (!isRipe(block))
            return false;

        List<ItemStack> drops = generateDrops(fortune);
        dropItems(block, drops);
        resetBlockAge(block);

        return true;
    }
}
