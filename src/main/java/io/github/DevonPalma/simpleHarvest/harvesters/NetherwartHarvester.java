package io.github.DevonPalma.simpleHarvest.harvesters;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NetherwartHarvester extends SimpleHarvester{
    public NetherwartHarvester() {
        super(Material.NETHER_WART);
        setPermission("simpleharvest.netherwart");
    }

    @Override
    public List<ItemStack> generateDrops(int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        int netherwart = (int) Math.round(Math.random() * (4 - 2) + 2);
        // account for replanting
        netherwart -= 1;

        netherwart += fortune;

        drops.add(new ItemStack(Material.NETHER_WART, netherwart));

        return drops;
    }
}
