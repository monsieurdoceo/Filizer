package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.util.List;
import java.util.Optional;

public final class FileManager {

    private static FileManager instance;
    private List<CustomFile> customFiles;
    private FileStorage fileStorage;

    private FileManager() {
        this.fileStorage = new FileStorage();
        this.customFiles = this.fileStorage.getFiles();
    }

    public static FileManager getInstance() {
        return instance == null ? instance = new FileManager() : instance;
    }

    private void addFile(String path, String name) {
        Optional<CustomFile> optionalFile = findFilebyName(name);
        if (optionalFile.isPresent()) return;

        this.customFiles.add(new CustomFile(path, name));
    }

    public void createFile(String path, String name) {
        addFile(path, name);
    }

    public void createFile(File parent, String name) {
        addFile(parent.getPath(), name);
    }

    public Optional<CustomFile> findFilebyName(String name) {
        return this.customFiles.stream()
            .filter(file -> file.getName().equalsIgnoreCase(name))
            .findAny();
    }

    public List<CustomFile> getFiles() {
        return this.customFiles;
    }
}
