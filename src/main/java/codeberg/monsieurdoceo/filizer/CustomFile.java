package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomFile {

    private String name;
    private FileCreator fileCreator;

    private File file;
    private FileConfiguration config;

    public CustomFile(String path, String name) {
        this.name = name;
        this.fileCreator = new FileCreator(path, name);

        this.file = this.fileCreator.getFile();
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public CustomFile(File parent, String name) {
        this.name = name;
        this.fileCreator = new FileCreator(parent, name);
    }

    public void set(String name, Object value) {
        this.config.set(name, value);
    }

    public void list(String name, List<Object> values) {
        this.config.set(name, values);
    }

    public String getName() {
        return this.name;
    }

    public FileCreator Creator() {
        return this.fileCreator;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
