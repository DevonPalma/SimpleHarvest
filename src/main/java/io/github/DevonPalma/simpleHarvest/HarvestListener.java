package io.github.DevonPalma.simpleHarvest;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class HarvestListener implements Listener {

    List<CropHarvester> cropDataList = new ArrayList<>();

    public HarvestListener() {
        cropDataList.add(CropHarvester.builder()
                .blockType(Material.WHEAT)
                .seedType(Material.WHEAT_SEEDS)
                .permission("simpleharvest.wheat")
                .build());

        cropDataList.add(CropHarvester.builder()
                .blockType(Material.CARROTS)
                .seedType(Material.CARROT)
                .permission("simpleharvest.carrot")
                .build());

        cropDataList.add(CropHarvester.builder()
                .blockType(Material.POTATOES)
                .seedType(Material.POTATO)
                .permission("simpleharvest.potato")
                .build());

        cropDataList.add(CropHarvester.builder()
                .blockType(Material.BEETROOTS)
                .seedType(Material.BEETROOT_SEEDS)
                .permission("simpleharvest.beetroot")
                .build());

        cropDataList.add(CropHarvester.builder()
                .blockType(Material.COCOA)
                .seedType(Material.COCOA_BEANS)
                .permission("simpleharvest.cocoa")
                .build());

        cropDataList.add(CropHarvester.builder()
                .blockType(Material.NETHER_WART)
                .seedType(Material.NETHER_WART)
                .permission("simpleharvest.netherwart")
                .breakSound(Sound.BLOCK_NETHER_WART_BREAK)
                .build());
    }


    @EventHandler
    public void blockInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        for (CropHarvester cropData : cropDataList) {
            if (cropData.tryHarvest(e)) {
                e.setCancelled(true);
                break;
            }
        }
    }
}
