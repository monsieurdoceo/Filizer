package codeberg.monsieurdoceo.filizer;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;

public final class FileStorage {

    private List<CustomFile> customFiles;

    public FileStorage() {
        this.customFiles = Lists.newArrayList();
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

    public List<CustomFile> getFiles() {
        return this.customFiles;
    }
}
