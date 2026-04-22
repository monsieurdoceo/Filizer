package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileGetter {

    private File file;
    private FileConfiguration config;

    public FileGetter(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
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
        if (path == null || path.isEmpty()) return getKeys(deep);

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

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
