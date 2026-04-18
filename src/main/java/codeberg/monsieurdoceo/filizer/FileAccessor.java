package codeberg.monsieurdoceo.filizer;

import java.util.List;
import java.util.Optional;

public final class FileAccessor {

    private FileStorage fileStorage;

    public FileAccessor() {
        this.fileStorage = FileStorage.getInstance();
    }

    public List<CustomFile> getFiles() {
        return this.fileStorage.getFiles();
    }

    public FileGetter Access(String name) {
        Optional<CustomFile> optionalFile = this.fileStorage.findFilebyName(
            name
        );
        if (optionalFile.isEmpty()) return null;

        CustomFile customFile = optionalFile.get();
        return new FileGetter(customFile.Creator().getFile());
    }
}
