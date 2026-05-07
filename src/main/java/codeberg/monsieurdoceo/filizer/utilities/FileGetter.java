package codeberg.monsieurdoceo.filizer.utilities;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public final class FileGetter {

    private FileConfiguration config;

    public FileGetter(FileConfiguration config) {
        this.config = config;
    }

    public Object get(String path) {
        return this.config.get(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public int getInteger(String path) {
        return this.config.getInt(path);
    }

    public List<?> list(String path) {
        return this.config.getList(path);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public Set<String> getKeys(boolean deep) {
        return this.config.getKeys(deep);
    }

    public Set<String> getKeys(String path, boolean deep) {
        if (path == null || path.isEmpty() || !has(path)) return getKeys(deep);

        ConfigurationSection section = this.config.getConfigurationSection(
            path
        );
        return (section != null)
            ? section.getKeys(deep)
            : Collections.emptySet();
    }

    public boolean has(String path) {
        return this.config.contains(path);
    }
}
