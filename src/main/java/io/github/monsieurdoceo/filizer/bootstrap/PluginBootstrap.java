package io.github.monsieurdoceo.filizer.bootstrap;

import io.github.monsieurdoceo.filizer.commands.DebugCommand;
import io.github.monsieurdoceo.filizer.shared.exceptions.FilizerExceptions;
import io.github.monsieurdoceo.filizer.shared.logging.AppLogger;
import io.github.monsieurdoceo.filizer.shared.logging.BukkitAppLogger;
import io.github.monsieurdoceo.filizer.storage.api.FileManager;
import io.github.monsieurdoceo.filizer.storage.infrastructure.FileRegistry;
import io.github.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;
import io.github.monsieurdoceo.filizer.storage.sync.strategy.LastModifiedStrategy;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bootstraps the Filizer plugin.
 *
 * <p>Responsible for initializing and wiring the plugin
 * components during startup and releasing resources during shutdown.
 */
public final class PluginBootstrap {

    /**
     * The plugin to bootstrap.
     */
    private final JavaPlugin plugin;

    /**
     * The logger instance.
     */
    private AppLogger logger;

    /**
     * The exception factory.
     */
    private FilizerExceptions errors;

    /**
     * The file registry.
     */
    private FileRegistry registry;

    /**
     * The file manager.
     */
    private FileManager fileManager;

    /**
     * Creates a new {@link PluginBootstrap} instance.
     * @param plugin The plugin to bootstrap
     */
    public PluginBootstrap(JavaPlugin plugin) {
        this.plugin = plugin;
    }

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
        this.fileManager = new FileManager(
            this.registry,
            strategy,
            this.logger,
            this.errors
        );

        // Register the commands
        registerCommands(this.fileManager);
    }

    /**
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
        if (checkCommand != null) checkCommand.setExecutor(
            new DebugCommand(fileManager)
        );
    }
}
