package codeberg.monsieurdoceo.filizer.managers;

import codeberg.monsieurdoceo.filizer.objects.CustomFile;
import codeberg.monsieurdoceo.filizer.objects.FileGetter;
import codeberg.monsieurdoceo.filizer.utilities.FileChecker;
import java.util.Optional;

public final class FileAccessor {

    private final FileStorage fileStorage = FileStorage.getInstance();

    /**
     * Attempts to retrieve the {@link FileGetter} associated with a file name.
     *
     * <p>If the provided file name is invalid or cannot be resolved,
     * an empty {@link Optional} is returned.
     *
     * @param name the file name to search for
     * @return an {@link Optional} containing the matching file getter,
     *         or an empty Optional if no match exists
     */
    public Optional<FileGetter> access(final String name) {
        return (FileChecker.hasValidName(name))
            ? this.fileStorage.findFileByName(name).map(
                  CustomFile::getFileGetter
              )
            : Optional.empty();
    }

    /**
     * Resolves the {@link FileGetter} associated with a file name.
     *
     * <p>This method behaves like {@link #access(String)} but throws an
     * {@link IllegalArgumentException} if the file cannot be resolved.
     *
     * @param name the file name to search for
     * @return the matching file getter
     * @throws IllegalArgumentException if the file name is invalid
     *                                  or no matching file exists
     */
    public FileGetter require(final String name) {
        return access(name).orElseThrow(() ->
            new IllegalArgumentException("[Filizer] File not found: " + name)
        );
    }
}
