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
    public Optional<FileGetter> access(String name) {
        return (FileChecker.checkingIfFileNameCorrect(name))
            ? this.fileStorage.findFilebyName(name).map(
                  CustomFile::getFileGetter
              )
            : Optional.empty();
    }

    public FileGetter require(final String name) {
        return access(name).orElseThrow(() ->
            new IllegalArgumentException("[Filizer] File not found: " + name)
        );
    }
}
