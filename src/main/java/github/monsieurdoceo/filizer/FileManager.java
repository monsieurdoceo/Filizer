package github.monsieurdoceo.filizer;

import java.util.List;

public final class FileManager {

    private static FileManager instance;
    private List<CustomFile> customFiles;

    private FileManager() {}

    public static FileManager getInstance() {
        if (instance == null) instance = new FileManager();

        return instance;
    }

    public List<CustomFile> getFiles() {
        return this.customFiles;
    }
}
