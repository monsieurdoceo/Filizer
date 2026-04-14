package codeberg.monsieurdoceo.filizer;

import java.util.List;
import java.util.Optional;

public final class FileAccessor {

    private static FileAccessor instance;
    private FileStorage fileStorage;

    private FileAccessor() {
        this.fileStorage = new FileStorage();
    }

    private CustomFile findCustomFileByName(String name) {
        Optional<CustomFile> optionalFile = this.fileStorage.findFilebyName(
            name
        );
        return optionalFile.isPresent() ? optionalFile.get() : null;
    }

    public static FileAccessor getInstance() {
        return instance == null ? instance = new FileAccessor() : instance;
    }

    public List<CustomFile> getFiles() {
        return this.fileStorage.getFiles();
    }

    public FileBuilder Builder(String name) {
        CustomFile customFile = findCustomFileByName(name);
        return new FileBuilder(customFile.Creator().getFile());
    }
}
