package codeberg.monsieurdoceo.filizer.managers;

import codeberg.monsieurdoceo.filizer.objects.CustomFile;
import codeberg.monsieurdoceo.filizer.utilities.FileGetter;

public final class FileAccessor {

    private final FileStorage fileStorage = FileStorage.getInstance();

    public FileGetter Access(String name) {
        return this.fileStorage.findFilebyName(name)
            .map(CustomFile::getFileGetter)
            .orElse(null);
    }
}
