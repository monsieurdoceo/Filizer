package github.monsieurdoceo.filizer;

import java.io.File;
import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class FileAccessor {

    private FileCreator fileCreator;
    private FileConfiguration config;

    public FileAccessor(FileCreator fileCreator) {
        this.fileCreator = fileCreator;
        if (!fileCreator.loadConfiguration()) {
            throw new IllegalStateException(
                "Filizer: Could not load configuration for file: " +
                    fileCreator.getFile().getPath() +
                    ". Cannot create FileAccessor."
            );
        }

        this.config = fileCreator.getConfig();
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public List<?> getList(String path) {
        return this.config.getList(path);
    }

    public ConfigurationSection getSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public Set<String> getKeys(String path, boolean deep) {
        ConfigurationSection section = (path == null || path.isEmpty())
            ? this.config
            : getSection(path);
        return section != null ? section.getKeys(deep) : Collections.emptySet();
    }

    public <T> List<T> loadAllSections(
        String path,
        BiFunction<String, ConfigurationSection, T> mapper
    ) {
        ConfigurationSection root = getSection(path);
        if (root == null) return Collections.emptyList();

        List<T> resultList = new ArrayList<>();
        for (String key : root.getKeys(false)) {
            ConfigurationSection section = root.getConfigurationSection(key);
            if (section != null) {
                T mappedObject = mapper.apply(key, section);
                if (mappedObject != null) resultList.add(mappedObject);
            }
        }
        return resultList;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
