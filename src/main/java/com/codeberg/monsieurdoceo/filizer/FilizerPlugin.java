package com.codeberg.monsieurdoceo.filizer;
import com.codeberg.monsieurdoceo.filizer.bootstrap.PluginBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public final class FilizerPlugin extends JavaPlugin {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private PluginBootstrap bootstrap;

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
