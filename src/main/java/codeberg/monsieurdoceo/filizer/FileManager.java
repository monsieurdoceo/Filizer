package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.util.List;
import java.util.Optional;

public final class FileManager {

    private static FileManager instance;
    private FileStorage fileStorage;

    private FileManager() {
        this.fileStorage = new FileStorage();
    }

    public static FileManager getInstance() {
        return instance == null ? instance = new FileManager() : instance;
    }

    private CustomFile addFile(String path, String name) {
        CustomFile customFile = new CustomFile(path, name);
        this.fileStorage.add(customFile);
        return customFile;
    }

    public CustomFile createFile(String path, String name) {
        return addFile(path, name);
    }

    public CustomFile createFile(File parent, String name) {
        return addFile(parent.getPath(), name);
    }

    public void removeFile(String name) {
        Optional<CustomFile> optionalFile = this.fileStorage.findFilebyName(
            name
        );
        if (optionalFile.isEmpty()) return;

        CustomFile customFile = optionalFile.get();
        this.fileStorage.remove(customFile);
    }

    public List<CustomFile> getFiles() {
        return this.fileStorage.getFiles();
    }
}
