package com.codeberg.monsieurdoceo.filizer.storage.infrastructure;

import com.codeberg.monsieurdoceo.filizer.storage.domain.CustomFile;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Path;

/**
 * Maintains the registry of managed files.
 *
 * <p>Stores, retrieves, and removes {@link CustomFile}
 * instances using their normalized file paths.
 */
public final class FileRegistry {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final Map<String, CustomFile> customFiles = new ConcurrentHashMap<>();

    /**********************************************************/
    /********************** CONSTRUCTORS **********************/
    /**********************************************************/

    /**
     * Initializes a new file registry instance.
     *
     * <p>No custom initialization is required.
     */
    public FileRegistry() {}

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Checks whether a file with the given {@link CustomFile} exists in storage.
     *
     * @param customFile the file to search for
     * @return {@code true} if file exists, otherwise {@code false}
     */
    public boolean contains(CustomFile customFile) {
        return customFile != null && this.customFiles.containsKey(keyOf(customFile.getFile().toPath()));
    }

    /**
     * Stores the given {@code CustomFile} if it is not already present.
     *
     * @param customFile the file to store
     */
    public void add(CustomFile customFile) {
        if(customFile != null) {
            this.customFiles.putIfAbsent(keyOf(customFile.getFile().toPath()), customFile);
        }
    }

    /**
     * Removes the {@link CustomFile} associated by the given name.
     *
     * @param name of the file to remove
     */
    public void remove(String name) {
        findByName(name).forEach(file -> this.customFiles.remove(keyOf(file.getFile().toPath())));
    }

    /**
     * Removes the {@link CustomFile} associated by the given path.
     *
     * @param path of the file to remove
     */
    public void remove(Path path) { this.customFiles.remove(keyOf(path)); }

    /**
     * Remove the given {@link CustomFile} from storage.
     *
     * <p>This method behaves like {@link #remove(String)} but operates
     * directly on a {@link CustomFile} instance.
     *
     * @param customFile the file to remove
     */
    public void remove(CustomFile customFile) {
        if(customFile != null) this.customFiles.remove(keyOf(customFile.getFile().toPath()));
    }

    /**
     * Retrieves a {@link CustomFile} by its absolute path.
     *
     * @param path the file path
     * @return the matching file, if present
     */
    public Optional<CustomFile> find(Path path) { return Optional.ofNullable(customFiles.get(keyOf(path))); }

    /**
     * Retrieves a {@link CustomFile} by its path.
     *
     * @param file the file
     * @return the matching file, if present
     */
    public Optional<CustomFile> find(File file) { return file == null ? Optional.empty() : find(file.toPath()); }

    /**
     * Retrieves every {@link CustomFile} matching the provided name.
     *
     * @param name the file name
     * @return all matching files
     */
    public Collection<CustomFile> findByName(String name) {
        return this.customFiles.values().stream()
                .filter(customFile -> customFile.getName().equals(name))
                .toList();
    }

    /**
     * Retrieves all stored {@link CustomFile} instances.
     *
     * @return a collection containing all stored files
     */
    public Collection<CustomFile> getFiles() { return this.customFiles.values(); }

    /**
     * Builds the storage key for a path.
     *
     * @param path the file path
     * @return the normalized absolute key
     */
    private static String keyOf(Path path) { return path.toAbsolutePath().normalize().toString(); }
}
