package com.codeberg.monsieurdoceo.filizer.storage.domain;

import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileFactory;
import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileReader;
import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileSynchronizer;
import com.codeberg.monsieurdoceo.filizer.shared.exceptions.FilizerExceptions;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Represents a managed configuration file.
 *
 * <p>A {@link CustomFile} provides a high-level API for creating,
 * reading, modifying, saving, and synchronizing YAML configuration files.
 */
public class CustomFile {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final String name;
    private final File file;
    private final FileSynchronizer synchronizer;
    private final FilizerExceptions errors;

    private FileConfiguration config;
    private FileReader reader;

    private long lastModified;

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Creates a new {@link CustomFile} from a parent path and file name.
     *
     * @param path the parent directory path
     * @param name the file name
     * @param synchronizer the synchronization service to use
     * @param errors the exception factory
     */
    public CustomFile(final Path path, final String name, final FileSynchronizer synchronizer, final FilizerExceptions errors) {
        this.name = name;
        this.file = FileFactory.createFile(path, name, Objects.requireNonNull(errors, "errors"));
        this.synchronizer = synchronizer;
        this.errors = errors;
        reload();
    }

    /**
     * Creates a new {@link CustomFile} from a parent directory and file name.
     *
     * @param parent the parent directory
     * @param name the file name
     * @param synchronizer the synchronization service to use
     * @param errors the exception factory
     */
    public CustomFile(final File parent, final String name, final FileSynchronizer synchronizer, final FilizerExceptions errors) {
        this.name = name;
        this.file = FileFactory.createFile(parent.toPath(), name, errors);
        this.synchronizer = synchronizer;
        this.errors = errors;
        reload();
    }

    /**
     * Saves the current configuration to disk.
     *
     * @throws IllegalStateException if the file cannot be saved
     */
    public void save() {
        try {
            this.config.save(this.file);
            this.lastModified = this.file.lastModified();
        } catch(Exception e) { throw this.errors.fileSaveFailed(this.name, e); }
    }

    /**
     * Reloads the file configuration from disk.
     *
     * <p>This method refreshes the underlying.
     *
     * <p>{@link FileConfiguration}, recreates the associated
     * <p>{@link FileReader}, and updates the cached
     * last-modified timestamp.
     */
    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.reader = new FileReader(this.config);
        this.lastModified = this.file.lastModified();
    }

    /**
     * Ensures that the file is synchronized with
     */
    private void sync() { if(this.synchronizer != null) this.synchronizer.ensureUpToDate(this); }

    /**
     * Retrieves the cached {@link FileReader} instance.
     *
     * <p>If the underlying file has been modified externally,
     * the configuration is automatically reloaded before
     * returning the file reader.
     *
     * @return the file reader
     */
    public FileReader getFileReader() {
        sync();
        return this.reader;
    }

    /**
     * Sets a value in the file configuration.
     *
     * @param name the configuration path
     * @param value the value to store
     * @return the current {@link CustomFile} instance
     */
    public CustomFile set(final String name, Object value) {
        sync();
        this.config.set(name, value);
        return this;
    }

    /**
     * Sets a list of values in the file configuration.
     *
     * @param name the configuration path
     * @param values the values to store
     * @return the current {@link CustomFile} instance
     */
    public CustomFile list(final String name, List<?> values) {
        sync();
        this.config.set(name, values);
        return this;
    }

    /**
     * Sets multiple values in the file configuration.
     *
     * <p>This method behaves like {@link #list(String, List)}
     * but accepts varargs values.
     *
     * @param name the configuration path
     * @param values the values to store
     * @return the current {@link CustomFile} instance
     */
    public CustomFile list(final String name, Object... values) {
        sync();
        this.config.set(name, Arrays.asList(values));
        return this;
    }

    /**
     * Creates and applies a configuration section.
     *
     * @param configSection the section to apply
     * @return the current {@link CustomFile} instance
     */
    public CustomFile section(final ConfigurationSectionBuilder configSection) {
        sync();
        configSection.createSection(this.config);
        return this;
    }

    /**
     * Retrieves the file name.
     *
     * @return the file name
     */
    public String getName() { return this.name; }

    /**
     * Retrieves the underlying file instance.
     *
     * @return the backing file
     */
    public File getFile() { return this.file; }

    /**
     * Returns the last modified timestamp of the file.
     *
     * @return the last modified timestamp
     */
    public long getLastModified() { return this.lastModified; }
}
