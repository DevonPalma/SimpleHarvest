package org.devonmc.simpleHarvest;

import org.devonmc.simpleHarvest.versionAPI.VersionAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleHarvest extends JavaPlugin {

    @Getter
    private static VersionAPI versionAPI;


    @Override
    public void onEnable() {
        super.onEnable();

        if (!setupNMS()) {
            getLogger().severe("Could not find NMS version for SimpleHarvest");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        getLogger().info("Simple harvest running!");
    }


    private String getFormattedVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        Matcher matcher = Pattern.compile("(?<Major>\\d+).(?<Minor>\\d+)(?:.(?<Patch>\\d+))?-R0.1-SNAPSHOT").matcher(bukkitVersion);
        matcher.find();
        String major = matcher.group("Major");
        String minor = matcher.group("Minor");
        String patch = matcher.group("Patch");

        return "v" + major + "_" + minor;
    }

    public boolean setupNMS() {
        String version = getFormattedVersion();

        try {
            Class<?> clazz = Class.forName("org.devonmc.simpleHarvest.versionAPI." + version + ".VersionAPIHandler");
            if (VersionAPI.class.isAssignableFrom(clazz))
                versionAPI = (VersionAPI) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

}
