package io.github.DevonPalma.simpleHarvest.harvesters;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BeetRootHarvester extends SimpleHarvester {

    public BeetRootHarvester() {
        super(Material.BEETROOTS);
        setPermission("simpleharvest.beetroot");
    }

    @Override
    public List<ItemStack> generateDrops(int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        drops.add(new ItemStack(Material.BEETROOT, 1));

        int seedCount = (int) Math.round(Math.random() * 3);
        // accounting for replanting
        seedCount -= 1;

        if (seedCount > 0)
            drops.add(new ItemStack(Material.BEETROOT_SEEDS, seedCount));

        return drops;
    }
}
