package io.github.DevonPalma.simpleHarvest.versionAPI.v1_13_2;

import io.github.DevonPalma.simpleHarvest.versionAPI.VersionAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VersionAPIHandler implements VersionAPI {

    public VersionAPIHandler() {
        // Block.getDrops does not provide seeds
        throw new Error("There is currently an issue with 1.13.2, currently fixing it");
    }

    @Override
    public boolean isHarvestable(Block block) {
        switch (block.getType()) {
            case COCOA:
            case WHEAT:
            case CARROTS:
            case POTATOES:
            case BEETROOTS:
            case NETHER_WART:
                return true;
        }
        return false;
    }

    @Override
    public boolean isRipe(Block block) {
        BlockData blockData = block.getBlockData();
        Ageable ageable = (Ageable) blockData;
        return ageable.getAge() == ageable.getMaximumAge();
    }

    @Override
    public List<ItemStack> getDrops(Player player, Block block) {
        EntityEquipment equipment = player.getEquipment();

        if (equipment.getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            return new ArrayList<>(block.getDrops(equipment.getItemInMainHand()));
        } else if (equipment.getItemInOffHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            return new ArrayList<>(block.getDrops(equipment.getItemInMainHand()));
        }

        return new ArrayList<>(block.getDrops());
    }

    @Override
    public ItemStack getSeed(Material material) {
        Material seedMaterial = null;
        switch (material) {
            case COCOA:
                seedMaterial = Material.COCOA_BEANS;
                break;
            case WHEAT:
                seedMaterial = Material.WHEAT_SEEDS;
                break;
            case CARROTS:
                seedMaterial = Material.CARROT;
                break;
            case POTATOES:
                seedMaterial = Material.POTATO;
                break;
            case BEETROOTS:
                seedMaterial = Material.BEETROOT_SEEDS;
                break;
            case NETHER_WART:
                seedMaterial = Material.NETHER_WART;
                break;
        }
        return seedMaterial == null ? null : new ItemStack(seedMaterial, 1);
    }

    @Override
    public void resetAge(Block block) {
        BlockData blockData = block.getBlockData();
        Ageable ageable = (Ageable) blockData;
        ageable.setAge(0);
        block.setBlockData(ageable);
    }
}
