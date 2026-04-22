package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.bukkit.Bukkit;

public final class FileManager {

    private FileStorage fileStorage;

    public FileManager() {
        this.fileStorage = FileStorage.getInstance();
    }

    private CustomFile addFile(String path, String name) {
        CustomFile customFile = new CustomFile(path, name);
        customFile.save();

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
        this.fileStorage.remove(name);
    }

    public void removeFile(CustomFile customFile) {
        this.fileStorage.remove(customFile);
    }

    public void storeAllFiles(Path root) throws IOException {
        if (Files.notExists(root)) Files.createDirectories(root);

        try (Stream<Path> paths = Files.walk(root)) {
            paths
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    addFile(
                        path.getParent().toString(),
                        path.getFileName().toString()
                    );
                });
        }
    }

    public boolean deleteFile(String name) {
        CustomFile customFile = this.fileStorage.findFilebyName(name).orElse(
            null
        );
        return customFile != null && deleteFile(customFile);
    }

    public boolean deleteFile(CustomFile customFile) {
        if (customFile == null) return false;

        try {
            Path path = customFile.Creator().getFile().toPath();
            boolean wasDeleted = Files.deleteIfExists(path);
            if (wasDeleted) {
                removeFile(customFile);
                return true;
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(
                "[Filizer]: IO Error deleting " +
                    customFile.getName() +
                    ": " +
                    e.getMessage()
            );
        }
        return false;
    }
}
