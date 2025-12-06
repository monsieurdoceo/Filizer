package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class FilizerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Filizer has been enabled!");
        registerCommand("test", new TestCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Filizer has been disabled!");
    }
}
