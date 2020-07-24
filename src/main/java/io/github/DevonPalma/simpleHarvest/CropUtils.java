package io.github.DevonPalma.simpleHarvest;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

public class CropUtils {

    public static boolean isRipe(Block block) {
        BlockData blockData = block.getBlockData();
        if (blockData == null)
            return false;
        if (!(blockData instanceof Ageable))
            return false;
        Ageable ageable = (Ageable) blockData;
        return ageable.getAge() == ageable.getMaximumAge();
    }

    public static void setBlockAge(Block block, int age) {
        BlockData blockData = block.getBlockData();
        Ageable ageable = (Ageable) blockData;
        ageable.setAge(age);
        block.setBlockData(ageable);
    }

}
