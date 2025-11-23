package github.monsieurdoceo.filizer;

import org.bukkit.plugin.java.JavaPlugin;

public class FilizerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Filizer has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Filizer has been disabled!");
    }
}
