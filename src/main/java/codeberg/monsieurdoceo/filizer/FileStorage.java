package codeberg.monsieurdoceo.filizer;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class FileStorage {

    private final Map<String, CustomFile> customFiles =
        new ConcurrentHashMap<>();
    private static final FileStorage instance = new FileStorage();

    public void add(CustomFile customFile) {
        this.customFiles.put(customFile.getName(), customFile);
    }

    public void remove(String name) {
        this.customFiles.remove(name);
    }

    public void remove(CustomFile customFile) {
        if (customFile != null) this.customFiles.remove(customFile.getName());
    }

    public Optional<CustomFile> findFilebyName(String name) {
        return (name != null)
            ? Optional.ofNullable(this.customFiles.get(name))
            : Optional.empty();
    }

    public Collection<CustomFile> getFiles() {
        return this.customFiles.values();
    }

    public static FileStorage getInstance() {
        return instance;
    }
}
