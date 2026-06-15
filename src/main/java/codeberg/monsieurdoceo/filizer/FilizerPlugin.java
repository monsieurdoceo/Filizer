package codeberg.monsieurdoceo.filizer;

import org.bukkit.plugin.java.JavaPlugin;

public final class FilizerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommand("check", new TestCommand());
    }
}
