package io.github.DevonPalma.simpleHarvest.versionAPI.v1_12_2;

import io.github.DevonPalma.simpleHarvest.versionAPI.VersionAPI;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VersionAPIHandler implements VersionAPI {

    private List<CropType> cropTypes;

    private CropType getCropBlock(Material material){
        for (CropType cropType : cropTypes) {
            if (cropType.blockMaterial == material) {
                return cropType;
            }
        }
        return null;
    }
    private CropType getCropSeed(Material material){
        for (CropType cropType : cropTypes) {
            if (cropType.seedMaterial == material) {
                return cropType;
            }
        }
        return null;
    }


    private class CropType {
        public Material blockMaterial;
        public Material seedMaterial;

        public IsRipe isRipe = isCropRipe;
        public Dropper dropper;
        public AgeReseter ageReseter = resetCropAge;

        public CropType(Material blockMaterial, Material seedMaterial) {
            this.blockMaterial = blockMaterial;
            this.seedMaterial = seedMaterial;
        }

        public CropType setRipeChecker(IsRipe isRipe) {
            this.isRipe = isRipe;
            return this;
        }

        public CropType setDropper(Dropper dropper) {
            this.dropper = dropper;
            return this;
        }

        public CropType setAgeReset(AgeReseter ageReset) {
            this.ageReseter = ageReset;
            return this;
        }

        public ItemStack getSeed() {
            return new ItemStack(seedMaterial, 1, (short) (seedMaterial == Material.COCOA ? 3 : 0));
        }
    }


    private interface IsRipe {
        public boolean isRipe(MaterialData materialData);
    }

    private interface Dropper {
        public List<ItemStack> getDrops(int fortune);
    }

    private interface AgeReseter {
        public void reset(MaterialData materialData);
    }


    public VersionAPIHandler() {
        cropTypes = new ArrayList<>();
        cropTypes.add(new CropType(Material.COCOA, Material.INK_SACK)
                .setRipeChecker(isCocoaRipe)
                .setDropper(getCocoaDrop)
                .setAgeReset(resetCocoaAge));

        cropTypes.add(new CropType(Material.CROPS, Material.SEEDS)
                .setDropper(getCropsDrop));

        cropTypes.add(new CropType(Material.CARROT, Material.CARROT_ITEM)
                .setDropper(getCarrotDrop));

        cropTypes.add(new CropType(Material.POTATO, Material.POTATO_ITEM)
                .setDropper(getPotatoDrop));

        cropTypes.add(new CropType(Material.BEETROOT_BLOCK, Material.BEETROOT_SEEDS)
                .setDropper(getBeetrootDrop));

        cropTypes.add(new CropType(Material.NETHER_WARTS, Material.NETHER_STALK)
                .setRipeChecker(isNetherwartRipe)
                .setDropper(getNetherwartDrop)
                .setAgeReset(resetNetherwartAge));
    }

    @Override
    public boolean isHarvestable(Block block) {
        return getCropBlock(block.getType()) != null;
    }




    @Override
    public boolean isRipe(Block block) {
        CropType cropType = getCropBlock(block.getType());
        if (cropType == null) return false;

        return cropType.isRipe.isRipe(block.getState().getData());
    }

    private IsRipe isCropRipe = (MaterialData materialData) -> {
        Crops crops = (Crops) materialData;
        return crops.getState() == CropState.RIPE;
    };

    private IsRipe isCocoaRipe = (MaterialData materialData) -> {
        CocoaPlant cocoaPlant = (CocoaPlant) materialData;
        return cocoaPlant.getSize() == CocoaPlant.CocoaPlantSize.LARGE;
    };

    private IsRipe isNetherwartRipe = (MaterialData materialData) -> {
        NetherWarts netherWarts = (NetherWarts) materialData;
        return netherWarts.getState() == NetherWartsState.RIPE;
    };




    private int getFortune(ItemStack item) {
        if (item != null)
            return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        return 0;
    }

    @Override
    public List<ItemStack> getDrops(Player player, Block block) {
        CropType cropType = getCropBlock(block.getType());
        if (cropType == null) return null;

        EntityEquipment eqp = player.getEquipment();

        int fortune = Math.max(
                getFortune(eqp.getItemInMainHand()),
                getFortune(eqp.getItemInOffHand()));

        return cropType.dropper.getDrops(fortune);
    }

    private int getRandomSeedCount(int fortune) {
        int seedCount = 0;
        for (int i = 0; i < 3 + fortune; i++) {
            if (ThreadLocalRandom.current().nextInt(15) <= 7) {
                seedCount += 1;
            }
        }
        return seedCount;
    }

    private Dropper getCropsDrop = (int fortune) -> {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Material.WHEAT, 1));

        drops.add(new ItemStack(Material.SEEDS, getRandomSeedCount(fortune)));
        return drops;
    };

    private Dropper getCarrotDrop = (int fortune) -> {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Material.CARROT_ITEM, 1 + ThreadLocalRandom.current().nextInt(4)));
        return drops;
    };

    private Dropper getPotatoDrop = (int fortune) -> {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Material.POTATO_ITEM, 2 + getRandomSeedCount(fortune)));

        if (ThreadLocalRandom.current().nextInt(100) <= 2)
            drops.add(new ItemStack(Material.POISONOUS_POTATO, 1));

        return drops;
    };

    private Dropper getBeetrootDrop = (int fortune) -> {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Material.BEETROOT, 1));

        drops.add(new ItemStack(Material.BEETROOT_SEEDS, getRandomSeedCount(fortune)));
        return drops;
    };

    private Dropper getCocoaDrop = (int fortune) -> {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Material.INK_SACK, 3, (short) 3));
        return drops;
    };

    private Dropper getNetherwartDrop = (int fortune) -> {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Material.NETHER_STALK, 2 + ThreadLocalRandom.current().nextInt(2)));
        return drops;
    };


    @Override
    public ItemStack getSeed(Material material) {
        CropType cropType = getCropBlock(material);
        if (cropType == null) return null;
        return cropType.getSeed();
    }


    @Override
    public void resetAge(Block block) {
        CropType cropType = getCropBlock(block.getType());
        if (cropType == null) return;

        BlockState blockState = block.getState();
        MaterialData materialData = blockState.getData();
        cropType.ageReseter.reset(materialData);
        blockState.setData(materialData);
        blockState.update();
    }

    private AgeReseter resetCropAge = (MaterialData materialData) -> {
        Crops crops = (Crops) materialData;
        crops.setState(CropState.SEEDED);
    };

    private AgeReseter resetCocoaAge = (MaterialData materialData) -> {
        CocoaPlant cocoaPlant = (CocoaPlant) materialData;
        cocoaPlant.setSize(CocoaPlant.CocoaPlantSize.SMALL);
    };

    private AgeReseter resetNetherwartAge = (MaterialData materialData) -> {
        NetherWarts netherWarts = (NetherWarts) materialData;
        netherWarts.setState(NetherWartsState.SEEDED);
    };

}
