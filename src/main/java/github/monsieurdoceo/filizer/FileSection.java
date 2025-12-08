package github.monsieurdoceo.filizer;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class FileSection {

    private String name;
    private Map<String, Object> data;

    public FileSection(String name) {
        this.name = name;
        this.data = new HashMap<>();
    }

    public FileSection set(String name, Object value) {
        if (name == null || name.trim().isEmpty()) return this;

        this.data.put(name, value);
        return this;
    }

    public void createSection(FileConfiguration config) {
        if (config == null) return;

        config.createSection(this.name, this.data);
    }

    public String getName() {
        return this.name;
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public boolean hasValidName() {
        return this.name != null && !this.name.trim().isEmpty();
    }
}
