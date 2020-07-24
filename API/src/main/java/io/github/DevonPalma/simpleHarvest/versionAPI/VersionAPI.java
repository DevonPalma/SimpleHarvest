package io.github.DevonPalma.simpleHarvest.versionAPI;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface VersionAPI {

    public boolean isHarvestable(Block block);

    public boolean isRipe(Block block);

    public List<ItemStack> getDrops(Player player, Block block);

    public ItemStack getSeed(Material material);

    public void resetAge(Block block);

}
