package codeberg.monsieurdoceo.filizer.managers;

import codeberg.monsieurdoceo.filizer.objects.CustomFile;
import codeberg.monsieurdoceo.filizer.utilities.FileChecker;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class FileStorage {

    private final Map<String, CustomFile> customFiles =
        new ConcurrentHashMap<>();
    private static final FileStorage instance = new FileStorage();

    /**
     * Checks whether a file with the given {@link CustomFile} exists in storage.
     *
     * @param customFile the file to search for
     * @return {@code true} if file exists, otherwise {@code false}
*/
    public boolean contains(CustomFile customFile) {
        return customFile != null && this.customFiles.containsKey(customFile.getName());
    }

    /**
    * Stores the given {@code CustomFile} if it is not already present.
    *
    * @param customFile the file to store
 */
    public void add(CustomFile customFile) {
        this.customFiles.putIfAbsent(customFile.getName(), customFile);
    }

    /**
    * Removes the {@link CustomFile} associated by the given name.
    *
    * @param name of the file to remove
 */
    public void remove(String name) {
        this.customFiles.remove(name);
    }

    /**
    * Remove the given {@link CustomFile} from storage.
    *
    * <p>This method behaves like {@link #remove(String)} but operates
    * directly on a {@link CustomFile} instance.
    *
    * @param customFile the file to remove
 */
    public void remove(CustomFile customFile) {
        if (customFile != null) this.customFiles.remove(customFile.getName());
    }

    /**
     * Attempts to retrieve a stored {@link CustomFile} by name.
     *
     * <p>If the provided name is invalid or no file matches,
     * an empty {@link Optional} is returned.
     *
     * @param name the file name to search for
     * @return an optional containing the matching custom file
*/
    public Optional<CustomFile> findFileByName(String name) {
        return (FileChecker.hasValidName(name))
            ? Optional.ofNullable(this.customFiles.get(name))
            : Optional.empty();
    }

    /**
     * Retrieves all stored {@link CustomFile} instances.
     *
     * @return a collection containing all stored files
*/
    public Collection<CustomFile> getFiles() {
        return this.customFiles.values();
    }

    /**
     * Retrieves the shared {@link FileStorage} instance.
     *
     * @return the singleton file storage instance
*/
    public static FileStorage getInstance() {
        return instance;
    }
}
