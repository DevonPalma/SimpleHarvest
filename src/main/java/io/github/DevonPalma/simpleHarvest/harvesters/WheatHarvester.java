package io.github.DevonPalma.simpleHarvest.harvesters;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WheatHarvester extends SimpleHarvester {

    public WheatHarvester() {
        super(Material.WHEAT);
        setPermission("simpleharvest.wheat");
    }

    @Override
    public List<ItemStack> generateDrops(int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        drops.add(new ItemStack(Material.WHEAT, 1));

        // Generates seeds based on minecraft wiki description
        int seedCount = 0;
        for (int i = 0; i < 3 + fortune; i++) {
            double rand = Math.random();
            if (rand > 0.57) {
                seedCount += 1;
            }
        }
        // offsets for replanting the seed itself
        // in theory this could be -1 but such a minimal
        // edge case that idc to work around it
        seedCount -= 1;
        if (seedCount > 0)
            drops.add(new ItemStack(Material.WHEAT_SEEDS, seedCount));

        return drops;
    }
}
