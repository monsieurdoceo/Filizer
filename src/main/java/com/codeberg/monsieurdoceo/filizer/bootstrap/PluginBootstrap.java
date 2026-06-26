package com.codeberg.monsieurdoceo.filizer.bootstrap;

import com.codeberg.monsieurdoceo.filizer.commands.TestCommand;
import com.codeberg.monsieurdoceo.filizer.storage.api.FileManager;
import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileRegistry;
import com.codeberg.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;
import com.codeberg.monsieurdoceo.filizer.storage.sync.strategy.LastModifiedStrategy;
import com.codeberg.monsieurdoceo.filizer.shared.exceptions.FilizerExceptions;
import com.codeberg.monsieurdoceo.filizer.shared.logging.AppLogger;
import com.codeberg.monsieurdoceo.filizer.shared.logging.BukkitAppLogger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginBootstrap {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final JavaPlugin plugin;
    private AppLogger logger;
    private FilizerExceptions errors;
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

        /* <!> Initialize the logger and error handler <!> */
        this.logger = new BukkitAppLogger(this.plugin.getLogger());
        this.errors = new FilizerExceptions(this.logger);

        /* <!> Initialize the file manager <!> */
        this.registry = new FileRegistry();
        FileSynchronizationStrategy strategy = new LastModifiedStrategy(); // <- Use 'LastModifiedStrategy'
        this.fileManager = new FileManager(this.registry, strategy, this.logger, this.errors);

        // Register the commands
        registerCommands(this.fileManager);
    }

    /*
     * Stops the plugin.
     */
    public void stop() {
        this.logger = null;
        this.errors = null;
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
