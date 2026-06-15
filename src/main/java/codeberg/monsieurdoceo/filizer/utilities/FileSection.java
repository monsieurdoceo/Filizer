package codeberg.monsieurdoceo.filizer.utilities;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents a configuration section that can be created
 * and populated inside a {@link FileConfiguration}.
 *
 * <p>A {@code FileSection} stores key-value pairs internally
 * until {@link #createSection(FileConfiguration)} is called.
 */
public class FileSection {

    private final String name;
    private Map<String, Object> data;

    /**
    * Creates a new empty file section.
    *
    * @param name the name of the configuration section
*/
    public FileSection(String name) {
        this.name = name;
        this.data = Maps.newHashMap();
    }

    /**
    * Adds or replaces a value in this section.
    *
    * @param name the key to store
    * @param value the value associated with the key
    * @return this section instance for chaining
*/
    public FileSection set(String name, Object value) {
        this.data.put(name, value);
        return this;
    }

    /**
    * Replaces the current section data.
    *
    * @param data the new section data
    * @return this section instance for chaining
*/
    public FileSection section(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    /**
    * Creates this section in the provided configuration.
    *
    * <p>If the configuration is {@code null},
    * no action is performed.
    *
    * @param config the configuration in which to create the section
*/
    public void createSection(FileConfiguration config) {
        if (config == null) return;

        config.createSection(this.name, this.data);
    }

    /**
    * Returns the name of this section.
    *
    * @return the section name
*/
    public String getName() {
        return this.name;
    }

    /**
    * Returns the data stored in this section.
    *
    * @return the section's key-value mappings
*/
    public Map<String, Object> getData() {
        return this.data;
    }
}
