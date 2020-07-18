package io.github.DevonPalma.simpleHarvest.harvesters;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PotatoHarvester extends SimpleHarvester {

    public PotatoHarvester() {
        super(Material.POTATOES);
        setPermission("simpleharvest.potato");
    }

    @Override
    public List<ItemStack> generateDrops(int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        int potatoCount = (int) Math.floor(Math.random() * (5 - 1) + 1);
        // accounting for replant cost
        potatoCount -= 1;

        if (potatoCount > 0)
            drops.add(new ItemStack(Material.POTATO, potatoCount));


        if (Math.random() <= 0.02) {
            drops.add(new ItemStack(Material.POISONOUS_POTATO, 1));
        }

        return drops;
    }
}
