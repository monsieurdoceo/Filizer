package codeberg.monsieurdoceo.filizer.objects;

import codeberg.monsieurdoceo.filizer.utilities.FileSection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomFile {

    private String name;

    private File file;
    private FileConfiguration config;

    private FileGetter fileGetter;
    private long lastModified;

    private void init(FileCreator fileCreator) {
        this.file = fileCreator.getFile();
        reload();
    }

    public CustomFile(Path path, String name) {
        this.name = name;
        init(new FileCreator(path, name));
    }

    public CustomFile(File parent, String name) {
        this.name = name;
        init(new FileCreator(parent, name));
    }

    public CustomFile set(String name, Object value) {
        this.config.set(name, value);
        return this;
    }

    public CustomFile list(String name, List<Object> values) {
        this.config.set(name, values);
        return this;
    }

    public CustomFile list(String name, Object... values) {
        this.config.set(name, values);
        return this;
    }

    public CustomFile section(FileSection fileSection) {
        fileSection.createSection(this.config);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }

    public void save() {
        try {
            this.config.save(this.file);
            this.lastModified = this.file.lastModified();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.fileGetter = new FileGetter(this.config);

        this.lastModified = this.file.lastModified();
    }

    public FileGetter getFileGetter() {
        long currentTime = this.file.lastModified();
        if (currentTime > this.lastModified) reload();

        return this.fileGetter;
    }
}
