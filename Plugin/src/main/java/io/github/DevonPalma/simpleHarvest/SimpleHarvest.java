package io.github.DevonPalma.simpleHarvest;

import io.github.DevonPalma.simpleHarvest.versionAPI.VersionAPI;
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
        Matcher matcher = Pattern.compile("([\\d.]+)-R0.1-SNAPSHOT").matcher(bukkitVersion);
        matcher.find();
        String version = matcher.group(1);
        version = "v" + version.replace(".", "_");
        return version;
    }

    public boolean setupNMS() {
        String version = getFormattedVersion();

        try {
            Class<?> clazz = Class.forName("io.github.DevonPalma.simpleHarvest.versionAPI." + version + ".NMSHandler");
            if (VersionAPI.class.isAssignableFrom(clazz))
                versionAPI = (VersionAPI) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

}
