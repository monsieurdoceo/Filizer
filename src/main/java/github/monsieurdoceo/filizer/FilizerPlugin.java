package github.monsieurdoceo.filizer;

import org.bukkit.plugin.java.JavaPlugin;

public class FilizerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Filizer has been enabled!");
        FileManager fileManager = FileManager.getInstance();

        registerCommand("check", new TestCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Filizer has been disabled!");
    }
}
