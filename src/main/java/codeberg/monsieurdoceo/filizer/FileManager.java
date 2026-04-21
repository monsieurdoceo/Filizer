package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.util.List;
import java.util.Optional;

public final class FileManager {

    private FileStorage fileStorage;

    public FileManager() {
        this.fileStorage = FileStorage.getInstance();
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

    public void removeFile(CustomFile customFile) {
        this.fileStorage.remove(customFile);
    }

    public void storeAllFiles(File parent) {
        if (!parent.exists()) parent.mkdir();

        File[] files = parent.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) storeAllFiles(file);
            else addFile(parent.getPath(), file.getName());
        }
    }

    public boolean deleteFile(String name) {
        Optional<CustomFile> optionalFile = this.fileStorage.findFilebyName(
            name
        );
        if (optionalFile.isEmpty()) return false;

        CustomFile customFile = optionalFile.get();
        return customFile.Creator().getFile().delete();
    }

    public boolean deleteFile(CustomFile customFile) {
        return customFile.Creator().getFile().delete();
    }

    public List<CustomFile> getFiles() {
        return this.fileStorage.getFiles();
    }
}
