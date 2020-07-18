package io.github.DevonPalma.simpleHarvest.harvesters;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CarrotHarvester extends SimpleHarvester {

    public CarrotHarvester() {
        super(Material.CARROTS);
        setPermission("simpleharvest.carrot");
    }

    @Override
    public List<ItemStack> generateDrops(int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        int carrotCount = (int) Math.floor(Math.random() * (5 - 1) + 1);
        // accounting for replant cost
        carrotCount -= 1;

        if (carrotCount > 0)
            drops.add(new ItemStack(Material.CARROT, carrotCount));

        return drops;
    }
}
