package codeberg.monsieurdoceo.filizer.managers;

import codeberg.monsieurdoceo.filizer.objects.CustomFile;
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
     * @param name of the file to search for
     * @return {@code true} if the file exists, {@code false} otherwise
     */
    public boolean containsFile(String name) {
        return this.fileStorage.findFileByName(name)
            .map(customFile -> this.fileStorage.contains(customFile))
            .isPresent();
    }

    private CustomFile addFile(Path path, String name) {
        CustomFile customFile = new CustomFile(path, name);
        this.fileStorage.add(customFile);
        return customFile;
    }

    /**
     * Create a newly file within a parent file by a path
     * and the name.
     *
     * @param path of the parent file to store
     * @param name of the file to create
     * @return a newly created {@link CustomFile}
     */
    public CustomFile createFile(String path, String name) {
        return addFile(Paths.get(path), name);
    }

    /**
     * Create a newly file within a parent file and
     * the name.
     *
     * <p>This method behaves like {@code createFile} but the parent file is a {@link File}.
     *
     * @param path of the parent file to store
     * @param name of the file to create
     * @return a newly created {@link CustomFile}
     */
    public CustomFile createFile(File parent, String name) {
        return addFile(parent.toPath(), name);
    }

    /**
    * Remove a {@link CustomFile} from the list by this name.
    *
    * @param name of the file to delete
 */
    public void removeFile(String name) {
        this.fileStorage.remove(name);
    }

    /**
    * Directly remove the {@link CustomFile} from the list.
    *
    * <p>This method behaves like {@link #removeFile(String)}
    * but directly get the {@link CustomFile}.
    *
    * @param customFile containing the information of the file
 */
    public void removeFile(CustomFile customFile) {
        this.fileStorage.remove(customFile);
    }

    /**
    * Retrieves the {@code File} from the {@code Path} given.
    *
    * <p>If {@code File} has been found through a filter to gather only files,
    * then it will store all of them in the list.
    *
    * @param root of a file path
    * @throws IOException if the files are null or cannot be stored
 */
    public void storeAllFiles(Path root) throws IOException {
        if (Files.notExists(root)) Files.createDirectories(root);

        try (Stream<Path> paths = Files.walk(root)) {
            paths
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    addFile(path.getParent(), path.getFileName().toString());
                });
        }
    }

    /**
    * Delete the {@link CustomFile} based on the given name.
    *
    * @param name of the file
    * @return {@code true} if the file has been deleted otherwise {@code false}
 */
    public boolean deleteFile(String name) {
        CustomFile customFile = this.fileStorage.findFileByName(name).orElse(
            null
        );
        return customFile != null && deleteFile(customFile);
    }

    /**
    * Delete the {@link CustomFile} based on the given name.
    *
    * <p>This method behaves like {@link #deleteFile(String)} but check if the {@link CustomFile}
    * is null and remove it from the list.
    *
    * @param customFile containing the information of the file
    * @return {@code true} if the file has been deleted otherwise {@code false}
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

    public FileStorage getFileStorage() {
        return this.fileStorage;
    }
}
