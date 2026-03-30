package codeberg.monsieurdoceo.filizer;

import java.util.List;

public final class FileAccessor {

    private static FileAccessor instance;
    private List<CustomFile> customFiles;
    private FileStorage fileStorage;

    private FileAccessor() {
        this.fileStorage = new FileStorage();
        this.customFiles = this.fileStorage.getFiles();
    }

    public static FileAccessor getInstance() {
        return instance == null ? instance = new FileAccessor() : instance;
    }
}
