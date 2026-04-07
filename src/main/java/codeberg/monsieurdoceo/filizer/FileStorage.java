package codeberg.monsieurdoceo.filizer;

import com.google.common.collect.Lists;
import java.util.List;

public final class FileStorage {

    private List<CustomFile> customFiles;

    public FileStorage() {
        this.customFiles = Lists.newArrayList();
    }

    public List<CustomFile> getFiles() {
        return this.customFiles;
    }
}
