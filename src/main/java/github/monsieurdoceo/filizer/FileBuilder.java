package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.FileCreator;
import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileBuilder {

    private FileCreator fileCreator;

    private File file;
    private FileConfiguration config;

    public FileBuilder(FileCreator fileCreator) {
        this.fileCreator = fileCreator;
        this.fileCreator.loadConfiguration();

        this.file = fileCreator.getFile();
        this.config = fileCreator.getConfig();
    }

    public FileBuilder set(String name, Object object) {
        if (name == null || name.isEmpty()) return this;
        if (!this.fileCreator.loadConfiguration()) return this;

        this.config.set(name, object);
        return this;
    }

    public File save() {
        try {
            this.config.save(this.file);
        } catch (IOException | IllegalArgumentException ex) {
            throw new RuntimeException("Error couldn't save the file.", ex);
        }
        return this.file;
    }
}
