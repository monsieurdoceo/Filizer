package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileBuilder {

    private File file;
    private FileConfiguration config;

    public FileBuilder(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Object get(String name) {
        return this.config.get(name);
    }

    public List<?> list(String name) {
        return this.config.getList(name);
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
