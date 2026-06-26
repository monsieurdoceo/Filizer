package com.codeberg.monsieurdoceo.filizer;
import com.codeberg.monsieurdoceo.filizer.bootstrap.PluginBootstrap;
import com.codeberg.monsieurdoceo.filizer.storage.sync.strategy.LastModifiedStrategy;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point of the Filizer plugin.
 *
 * <p>Coordinates the plugin lifecycle and delegates startup
 * and shutdown to the bootstrap.
 */
public final class FilizerPlugin extends JavaPlugin {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private PluginBootstrap bootstrap;

    /**********************************************************/
    /********************** CONSTRUCTORS **********************/
    /**********************************************************/

    /**
     * Initializes the Filizer plugin instance.
     *
     * <p>Serves as the main entry point of the Filizer plugin.
     * <p>No custom initialization is required.
     */
    public FilizerPlugin() {}

    /**********************************************************/
    /*********************** LIFE CYCLE ***********************/
    /**********************************************************/

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
        if(this.bootstrap != null) this.bootstrap.stop();
    }
}
