package com.codeberg.monsieurdoceo.filizer.storage.api;

import com.codeberg.monsieurdoceo.filizer.storage.domain.CustomFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileRegistry;
import com.codeberg.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;
import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileSynchronizer;
import com.codeberg.monsieurdoceo.filizer.shared.util.FileChecker;
import org.bukkit.Bukkit;

public final class FileManager {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final FileRegistry fileRegistry;
    private final FileSynchronizer synchronizer;

    /*********************************************************/
    /********************** CONSTRUCTOR **********************/
    /*********************************************************/

    /**
     * Constructs a new {@link FileManager} instance.
     * @param registry The file registry to use
     * @param strategy The synchronization strategy to use
     */
    public FileManager(FileRegistry registry, FileSynchronizationStrategy strategy) {
        this.fileRegistry = registry;
        this.synchronizer = new FileSynchronizer(strategy);
    }

    /*********************************************************/
    /*********************** FUNCTIONS *****    ******************/
    /*********************************************************/

    /**
     * Checks whether a file with the given name exists in storage.
     *
     * @param name the file name to search for
     * @return {@code true} if the file exists, {@code false} otherwise
     */
    public boolean containsFile(String name) { return findFile(name).isPresent(); }

    /**
     * Attempts to retrieve a registered file by its name.
     *
     * @param name the file name
     * @return the matching file, if present
     */
    public Optional<CustomFile> findFile(String name) {

        if(!FileChecker.hasValidName(name)) return Optional.empty();
        Collection<CustomFile> matches = this.fileRegistry.findByName(name);
        if(matches.size() == 1) return Optional.of(matches.iterator().next());
        if(matches.size() > 1) Bukkit.getLogger().warning("[Filizer] Ambiguous file name lookup ignored for: " + name);
        return Optional.empty();
    }

    /**
     * Attempts to retrieve a registered file by its path.
     *
     * @param path the parent directory path
     * @param name the file name
     * @return the matching file, if present
     */
    public Optional<CustomFile> findFile(Path path, String name) {
        if(path == null || !FileChecker.hasValidName(name)) return Optional.empty();
        return this.fileRegistry.find(path.resolve(name));
    }

    /**
     * Retrieves a registered file.
     *
     * @param name the file name
     * @return the matching file
     * @throws IllegalArgumentException if the file does not exist
     */
    public CustomFile requireFile(String name) {
        return findFile(name).orElseThrow(() -> new IllegalArgumentException("[Filizer] File not found: " + name));
    }
    /**
     * Adds a new {@link CustomFile} to storage if it does not already exist.
     *
     * @param path The path to the file
     * @param name The name of the file
     * @return The created or existing {@link CustomFile}
     */
    private CustomFile addFile(Path path, String name) {

        if(path == null || !FileChecker.hasValidName(name))
            throw new IllegalArgumentException("[Filizer] Invalid file path or name: " + path + "/" + name);

        return findFile(path, name).orElseGet(() -> {
            CustomFile customFile = new CustomFile(path, name, this.synchronizer);
            this.fileRegistry.add(customFile);
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
    public CustomFile createFile(String path, String name) { return addFile(Paths.get(path), name); }

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
    public CustomFile createFile(File parent, String name) { return addFile(parent.toPath(), name); }

    /**
     * Removes a {@link CustomFile} from storage by this name.
     *
     * @param name the file name
     */
    public void removeFile(String name) { this.fileRegistry.remove(name); }

    /**
     * Removes a file from storage by its path and name.
     *
     * @param path the parent path
     * @param name the file name
     */
    public void removeFile(Path path, String name) {
        if(path != null && FileChecker.hasValidName(name)) {
            this.fileRegistry.remove(path.resolve(name));
        }
    }

    /**
     * Removes the given {@link CustomFile} from storage.
     *
     * <p>This method behaves like {@link #removeFile(String)}
     * but operates directly on a {@link CustomFile} instance.
     *
     * @param customFile the file to remove
     */
    public void removeFile(CustomFile customFile) { this.fileRegistry.remove(customFile); }

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
        if(Files.notExists(root)) Files.createDirectories(root);
        try(Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile).forEach(path -> addFile(path.getParent(), path.getFileName().toString()));
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
        return findFile(name)
                .map(this::deleteFile)
                .orElse(false);
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

        if(customFile == null) return false;

        try {

            Path path = customFile.getFile().toPath();

            if(Files.deleteIfExists(path)) {
                removeFile(customFile);
                return true;
            }

        } catch(IOException e) {
            Bukkit.getLogger().severe("[Filizer] IO Error deleting " + customFile.getName() + ": " + e.getMessage());
        }

        return false;
    }

    /**
     * Retrieves the underlying file storage instance.
     *
     * @return the file storage
     */
    public FileRegistry getRegistry() { return this.fileRegistry; }

    /**
     * Retrieves the file synchronization instance.
     * @return the file synchronization instance
     */
    public FileSynchronizer getSynchronizer() { return this.synchronizer; }
}
