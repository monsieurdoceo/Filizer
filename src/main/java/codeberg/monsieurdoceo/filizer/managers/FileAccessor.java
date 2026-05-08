package codeberg.monsieurdoceo.filizer.managers;

import codeberg.monsieurdoceo.filizer.objects.CustomFile;
import codeberg.monsieurdoceo.filizer.objects.FileGetter;
import codeberg.monsieurdoceo.filizer.utilities.FileChecker;
import java.util.Optional;

public final class FileAccessor {

    private final FileStorage fileStorage = FileStorage.getInstance();

    /**
     * Retrieves a FileGetter by the file name
     * @param name File name
     * @return Optional containing the fileGetter if found
     */
    public Optional<FileGetter> access(final String name) {
        return (FileChecker.checkingIfFileNameCorrect(name))
            ? this.fileStorage.findFilebyName(name).map(
                  CustomFile::getFileGetter
              )
            : Optional.empty();
    }

    /**
     * Retrieves a FileGetter by the file name (the strict mode)
     * @param name File name
     * @return The fileGetter or throw an IllegalArgumentException
     */
    public FileGetter require(String name) {
        return access(name).orElseThrow(() ->
            new IllegalArgumentException("[Filizer] File not found: " + name)
        );
    }
}
