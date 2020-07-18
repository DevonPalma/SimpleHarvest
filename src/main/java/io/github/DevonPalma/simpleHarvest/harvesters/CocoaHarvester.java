package io.github.DevonPalma.simpleHarvest.harvesters;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CocoaHarvester extends SimpleHarvester {
    public CocoaHarvester() {
        super(Material.COCOA);
        setPermission("simpleharvest.cocoa");
    }

    @Override
    public List<ItemStack> generateDrops(int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        drops.add(new ItemStack(Material.COCOA_BEANS, 2));

        return drops;
    }
}
