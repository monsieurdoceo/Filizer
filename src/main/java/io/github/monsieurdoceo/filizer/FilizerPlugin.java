package io.github.monsieurdoceo.filizer;

import io.github.monsieurdoceo.filizer.bootstrap.PluginBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point of the Filizer plugin.
 *
 * <p>Coordinates the plugin lifecycle and delegates startup
 * and shutdown to the bootstrap.
 */
public final class FilizerPlugin extends JavaPlugin {

    /**
     * The plugin bootstrap instance.
     */
    private PluginBootstrap bootstrap;

    /**
     * Initializes the Filizer plugin instance.
     *
     * <p>Serves as the main entry point of the Filizer plugin.
     * <p>No custom initialization is required.
     */
    public FilizerPlugin() {}

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        this.bootstrap = new PluginBootstrap(this);
        this.bootstrap.start();
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        if (this.bootstrap != null) this.bootstrap.stop();
    }
}
