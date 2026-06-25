package com.codeberg.monsieurdoceo.filizer.bootstrap;

import com.codeberg.monsieurdoceo.filizer.commands.TestCommand;
import com.codeberg.monsieurdoceo.filizer.file.api.FileManager;
import com.codeberg.monsieurdoceo.filizer.file.infrastructure.FileRegistry;
import com.codeberg.monsieurdoceo.filizer.file.sync.FileSynchronizationStrategy;
import com.codeberg.monsieurdoceo.filizer.file.sync.strategy.LastModifiedStrategy;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginBootstrap {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final JavaPlugin plugin;
    private FileRegistry registry;
    private FileManager fileManager;

    /*********************************************************/
    /********************** CONSTRUCTOR **********************/
    /*********************************************************/

    /**
     * Creates a new {@link PluginBootstrap} instance.
     * @param plugin The plugin to bootstrap
     */
    public PluginBootstrap(JavaPlugin plugin) { this.plugin = plugin; }

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Starts the plugin.
     */
    public void start() {
        this.registry = new FileRegistry();
        FileSynchronizationStrategy strategy = new LastModifiedStrategy();
        this.fileManager = new FileManager(this.registry, strategy);
        registerCommands(this.fileManager);
    }

    /*
     * Stops the plugin.
     */
    public void stop() {
        this.fileManager = null;
        this.registry = null;
    }

    /**
     * Register the commands of the plugin.
     *
     * @param fileManager The file manager to use
     */
    private void registerCommands(FileManager fileManager) {
        PluginCommand checkCommand = plugin.getCommand("check");
        if(checkCommand != null) checkCommand.setExecutor(new TestCommand(fileManager));
    }
}
