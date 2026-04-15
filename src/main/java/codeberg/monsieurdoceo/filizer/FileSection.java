package codeberg.monsieurdoceo.filizer;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;

public class FileSection {

    private String name;
    private Map<String, Object> data;

    public FileSection(String name) {
        this.name = name;
        this.data = Maps.newHashMap();
    }

    public FileSection set(String name, Object value) {
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
}
