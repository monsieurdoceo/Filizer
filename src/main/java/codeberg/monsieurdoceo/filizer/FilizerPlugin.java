package codeberg.monsieurdoceo.filizer;

import org.bukkit.plugin.java.JavaPlugin;

public final class FilizerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Filizer has been enabled!");

        registerCommand("check", new TestCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Filizer has been disabled!");
    }
}
