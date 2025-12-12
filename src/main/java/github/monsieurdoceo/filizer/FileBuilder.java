package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.FileCreator;
import github.monsieurdoceo.filizer.FileSection;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileBuilder {

    private FileCreator fileCreator;

    private File file;
    private FileConfiguration config;

    public FileBuilder(FileCreator fileCreator) {
        this.fileCreator = fileCreator;
        if (!this.fileCreator.loadConfiguration()) {
            Bukkit.getLogger().warning(
                "Filizer: Could not load configuration for file: " +
                    fileCreator.getFile().getPath() +
                    ". Building a new configuration."
            );
            return;
        }

        this.file = fileCreator.getFile();
        this.config = fileCreator.getConfig();
    }

    public FileBuilder set(String name, Object value) {
        if (name == null || name.trim().isEmpty()) return this;

        this.config.set(name, value);
        return this;
    }

    public FileBuilder set(FileSection fileSection) {
        if (fileSection == null || !fileSection.hasValidName()) return this;

        this.config.createSection(fileSection.getName(), fileSection.getData());
        return this;
    }

    public FileBuilder list(String name, List<?> values) {
        if (name == null || name.trim().isEmpty()) return this;

        this.config.set(name, values);
        return this;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            throw new RuntimeException("Error couldn't save the file.", ex);
        }
    }
}
