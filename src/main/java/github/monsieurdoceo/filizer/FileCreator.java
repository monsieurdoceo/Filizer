package github.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileCreator {

    private File file;
    private YamlConfiguration config;

    private void createFile(File parent, String name) {
        this.file = new File(parent, name);
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(
                    "Couldn't create the file [" + name + "].",
                    ex
                );
            }
        }
    }

    public boolean loadConfiguration() {
        if (this.file == null || !this.file.exists()) return false;

        this.config = YamlConfiguration.loadConfiguration(this.file);
        return true;
    }

    public FileCreator(File parent, String name) {
        createFile(parent, name);
        loadConfiguration();
    }

    public FileCreator(String path, String name) {
        File parent = new File(path);
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                Bukkit.getLogger().info(
                    "Couldn't create the directory [" + name + "]."
                );
            }
        }

        createFile(parent, name);
        loadConfiguration();
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
