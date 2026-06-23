package com.codeberg.monsieurdoceo.filizer.managers;

import com.codeberg.monsieurdoceo.filizer.objects.CustomFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.bukkit.Bukkit;

public final class FileManager {

    private final FileStorage fileStorage;

    public FileManager() {
        this.fileStorage = FileStorage.getInstance();
    }

    /**
     * Checks whether a file with the given name exists in storage.
     *
     * @param name the file name to search for
     * @return {@code true} if the file exists, {@code false} otherwise
     */
    public boolean containsFile(String name) {
        return this.fileStorage.findFileByName(name).isPresent();
    }

    private CustomFile addFile(Path path, String name) {
        return this.fileStorage.findFileByName(name).orElseGet(() -> {
            CustomFile customFile = new CustomFile(path, name);
            this.fileStorage.add(customFile);
            return customFile;
        });
    }

    /**
     * Creates a new {@link CustomFile} from a parent path and a name.
     *
     * @param path the parent directory path
     * @param name the file name
     * @return the created {@link CustomFile}
     */
    public CustomFile createFile(String path, String name) {
        return addFile(Paths.get(path), name);
    }

    /**
     * Creates a new {@link CustomFile} from a parent directory and file name.
     *
     * <p>This method behaves like {@link #createFile(String, String)} but accepts
     * a {@link File} as the parent reference.
     *
     * @param parent the parent directory
     * @param name the file name
     * @return the created {@link CustomFile}
     */
    public CustomFile createFile(File parent, String name) {
        return addFile(parent.toPath(), name);
    }

    /**
     * Removes a {@link CustomFile} from storage by this name.
     *
     * @param name the file name
     */
    public void removeFile(String name) {
        this.fileStorage.remove(name);
    }

    /**
     * Removes the given {@link CustomFile} from storage.
     *
     * <p>This method behaves like {@link #removeFile(String)}
     * but operates directly on a {@link CustomFile} instance.
     *
     * @param customFile the file to remove
     */
    public void removeFile(CustomFile customFile) {
        this.fileStorage.remove(customFile);
    }

    /**
     * Recursively stores all regular files found under the given root path.
     *
     * <p>If the root path does not exist, it is created automatically.
     *
     * @param root the root directory to scan
     * @throws IOException if the directory cannot be created or files cannot
     * be accessed
     */
    public void storeAllFiles(Path root) throws IOException {
        if (Files.notExists(root)) Files.createDirectories(root);

        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                addFile(path.getParent(), path.getFileName().toString());
            });
        }
    }

    /**
     * Deletes the {@link CustomFile} associated with the given name.
     *
     * @param name file name
     * @return {@code true} if the file was deleted successfully,
     * otherwise {@code false}
     */
    public boolean deleteFile(String name) {
        CustomFile customFile = this.fileStorage
            .findFileByName(name)
            .orElse(null);
        return customFile != null && deleteFile(customFile);
    }

    /**
     * Deletes the given {@link CustomFile} from filesystem and removes it
     * from storage.
     *
     * <p>This method behaves like {@link #deleteFile(String)} but operates
     * directly on a {@link CustomFile} instance.
     *
     * @param customFile the file to delete
     * @return {@code true} if the file was deleted successfully,
     * otherwise {@code false}
     */
    public boolean deleteFile(CustomFile customFile) {
        if (customFile == null) return false;

        try {
            Path path = customFile.getFile().toPath();
            if (Files.deleteIfExists(path)) {
                removeFile(customFile);
                return true;
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(
                "[Filizer] IO Error deleting " +
                    customFile.getName() +
                    ": " +
                    e.getMessage()
            );
        }
        return false;
    }

    /**
     * Retrieves the underlying file storage instance.
     *
     * @return the file storage
     */
    public FileStorage getFileStorage() {
        return this.fileStorage;
    }
}
