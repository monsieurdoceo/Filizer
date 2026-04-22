package codeberg.monsieurdoceo.filizer;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;

public final class FileStorage {

    private Set<CustomFile> customFiles;
    private static FileStorage instance;

    private FileStorage() {
        this.customFiles = Sets.newHashSet();
    }

    public void add(CustomFile customFile) {
        this.customFiles.add(customFile);
    }

    public void remove(CustomFile customFile) {
        this.customFiles.remove(customFile);
    }

    public Optional<CustomFile> findFilebyName(String name) {
        return getFiles()
            .stream()
            .filter(file -> file.getName().equalsIgnoreCase(name))
            .findAny();
    }

    public Set<CustomFile> getFiles() {
        return this.customFiles;
    }

    public static FileStorage getInstance() {
        return instance == null ? instance = new FileStorage() : instance;
    }
}
