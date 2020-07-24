package io.github.DevonPalma.simpleHarvest;

import lombok.Builder;
import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleHarvest extends JavaPlugin {

    @Getter
    private static SimpleHarvest instance;
    @Getter
    private static Permission perms = null;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        if (!setupPermissions()) {
            getLogger().severe("Could not setup vault permission connection");
            getServer().getPluginManager().disablePlugin(this);
        }

        getServer().getPluginManager().registerEvents(new HarvestListener(), this);

        getLogger().info("SimpleHarvest is now running!");
    }



    private boolean setupPermissions() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) return false;
        perms = rsp.getProvider();
        //noinspection ConstantConditions
        return perms != null;
    }
}
