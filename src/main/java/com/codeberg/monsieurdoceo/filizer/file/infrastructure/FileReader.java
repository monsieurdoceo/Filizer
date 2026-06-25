package com.codeberg.monsieurdoceo.filizer.file.infrastructure;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class FileReader {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final FileConfiguration config;

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/


    /**
     * Creates a new {@link FileReader} from a configuration instance.
     *
     * @param config the backing configuration
     */
    public FileReader(FileConfiguration config) { this.config = config; }

    /**
     * Retrieves a raw value from the configuration.
     *
     * @param path the configuration path
     * @return the stored value, or {@code null} if absent
     */
    public Object get(String path) { return this.config.get(path); }

    /**
     * Retrieves a string value from the configuration.
     *
     * @param path the configuration path
     * @return the stored string, or {@code null} if absent
     */
    public String getString(String path) { return this.config.getString(path); }

    /**
     * Retrieves an integer value from the configuration.
     *
     * <p>If the path is absent or not an integer,
     * {@code 0} is returned.
     *
     * @param path the configuration path
     * @return the stored integer value
     */
    public int getInt(String path) { return this.config.getInt(path, 0); }

    /**
     * Retrieves an double value from the configuration.
     *
     * <p>If the path is absent or not an integer,
     * {@code 0.0} is returned.
     *
     * @param path the configuration path
     * @return the stored integer value
     */
    public double getDouble(String path) { return this.config.getDouble(path, 0.0); }

    /**
     * Retrieves a string list from the configuration.
     *
     * @param path the configuration path
     * @return the stored list, or {@code null} if absent
     */
    public List<String> getStringList(String path) { return this.config.getStringList(path); }

    /**
     * Retrieves an integer list from the configuration.
     *
     * @param path the configuration path
     * @return the stored list, or {@code null} if absent
     */
    public List<Integer> getIntegerList(String path) { return this.config.getIntegerList(path); }

    /**
     * Retrieves a configuration section.
     *
     * @param path the configuration path
     * @return the matching section, or {@code null} if absent
     */
    public ConfigurationSection getSection(String path) { return this.config.getConfigurationSection(path); }

    /**
     * Retrieves all keys from the root configuration.
     *
     * @param deep whether nested keys should be included
     * @return a set containing configuration keys
     */
    public Set<String> getKeys(boolean deep) { return this.config.getKeys(deep); }

    /**
     * Retrieves keys from a specific configuration section.
     *
     * <p>If the provided path is invalid or absent,
     * root configuration keys are returned instead.
     *
     * @param path the configuration section path
     * @param deep whether nested keys should be included
     * @return a set containing matching keys,
     * or an empty set if the section does not exist
     */
    public Set<String> getKeys(String path, boolean deep) {

        if(path == null || path.isEmpty() || !has(path)) return getKeys(deep);

        ConfigurationSection section = this.config.getConfigurationSection(path);
        return section != null ? section.getKeys(deep) : Collections.emptySet();
    }

    /**
     * Checks whether a configuration path exists.
     *
     * @param path the configuration path
     * @return {@code true} if the path exists,
     * otherwise {@code false}
     */
    public boolean has(String path) { return this.config.contains(path); }
}
