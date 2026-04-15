package codeberg.monsieurdoceo.filizer;

import java.util.List;
import java.util.Optional;

public final class FileAccessor {

    private FileStorage fileStorage;

    public FileAccessor() {
        this.fileStorage = FileStorage.getInstance();
    }

    private CustomFile findCustomFileByName(String name) {
        Optional<CustomFile> optionalFile = this.fileStorage.findFilebyName(
            name
        );
        return optionalFile.isPresent() ? optionalFile.get() : null;
    }

    public List<CustomFile> getFiles() {
        return this.fileStorage.getFiles();
    }

    public FileGetter Access(String name) {
        CustomFile customFile = findCustomFileByName(name);
        return new FileGetter(customFile.Creator().getFile());
    }
}
