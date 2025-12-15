package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.CustomFile;
import github.monsieurdoceo.filizer.FileAccessor;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FileManager {

    private Set<CustomFile> customFiles;

    public FileManager() {
        this.customFiles = new HashSet<>();
    }

    public void addFile(CustomFile customFile) {
        if (this.customFiles.contains(customFile)) return;

        this.customFiles.add(customFile);
    }

    public void removeFile(CustomFile customFile) {
        if (!this.customFiles.contains(customFile)) return;

        this.customFiles.removeIf(file ->
            file.getName().equalsIgnoreCase(customFile.getName())
        );
    }

    public Optional<CustomFile> findFileByName(String name) {
        return this.customFiles.stream()
            .filter(file -> file.getName().equalsIgnoreCase(name))
            .findAny();
    }

    // Impliment a better error catch
    public FileAccessor readFileByName(String name) {
        Optional<CustomFile> optionalFile = findFileByName(name);
        CustomFile customFile = optionalFile.get();
        return new FileAccessor(customFile.creator());
    }

    public Set<CustomFile> getCustomFiles() {
        return this.customFiles;
    }
}
