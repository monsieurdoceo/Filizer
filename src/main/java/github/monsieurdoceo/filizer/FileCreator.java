package github.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileCreator {

    private File file;
    private YamlConfiguration config;

    public FileCreator(String path, String name) {
        if (
            path == null ||
            path.trim().isEmpty() ||
            name == null ||
            name.trim().isEmpty()
        ) {
            throw new IllegalArgumentException(
                "Filizer: Path and file name cannot be empty or null. Cannot create the file."
            );
        }

        File parent = FileManager.getInstance().searchFolder(path);
        if (parent == null) return;

        createFile(parent, name);
    }

    private void createFile(File parent, String name) {
        this.file = new File(parent, name + ".yml");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException ex) {
                Bukkit.getLogger().severe(
                    "Filizer: Couldn't create file [" +
                        name +
                        "] in directory [" +
                        parent.getPath() +
                        "]."
                );
                ex.printStackTrace();
            }
        }
    }

    public boolean loadConfiguration() {
        if (this.file == null || !this.file.exists()) return false;

        this.config = YamlConfiguration.loadConfiguration(this.file);
        return true;
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
