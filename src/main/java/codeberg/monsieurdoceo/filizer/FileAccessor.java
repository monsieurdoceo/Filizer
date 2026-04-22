package codeberg.monsieurdoceo.filizer;

public final class FileAccessor {

    private final FileStorage fileStorage = FileStorage.getInstance();

    public FileGetter Access(String name) {
        return this.fileStorage.findFilebyName(name)
            .map(CustomFile::getFileGetter)
            .orElse(null);
    }
}
