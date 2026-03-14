package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.CustomFile;
import github.monsieurdoceo.filizer.FileAccessor;
import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Bukkit;

public class FileManager {

    private static FileManager instance;
    private Set<CustomFile> customFiles;

    private FileManager() {
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

    public void removeFileByName(String name) {
        Optional<CustomFile> optionalFile = findFileByName(name);
        if (optionalFile.isEmpty()) return;

        this.customFiles.removeIf(file ->
            file.getName().equalsIgnoreCase(name)
        );
    }

    public Optional<CustomFile> findFileByName(String name) {
        return this.customFiles.stream()
            .filter(file -> file.getName().equalsIgnoreCase(name))
            .findAny();
    }

    // Impliment a better error catch system
    public FileAccessor readFileByName(String name) {
        Optional<CustomFile> optionalFile = findFileByName(name);
        CustomFile customFile = optionalFile.get();
        return new FileAccessor(customFile.creator());
    }

    // Impliment a better error catch system | create a better searching tool to find folder
    public File searchFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Bukkit.getLogger().severe(
                    "Filizer: Failed to create directory [" +
                        path +
                        "]. Cannot initialize file."
                );
                return null;
            }
        } else if (!folder.isDirectory()) {
            Bukkit.getLogger().severe(
                "Filizer: Path [" +
                    path +
                    "] is an existing file, not a directory. Cannot initialize file."
            );
            return null;
        }

        return folder;
    }

    public void storeAllFilesFromFolder(String path) {
        File parent = searchFolder(path);
        if (parent == null) return;

        File[] files = parent.listFiles();
        if (files == null) return;

        for (File file : files) {
            CustomFile customFile = new CustomFile(path, file.getName());
            addFile(customFile);
        }
    }

    public boolean deleteFile(String name) {
        Optional<CustomFile> optionalFile = findFileByName(name);
        if (optionalFile.isEmpty()) return false;

        CustomFile customFile = optionalFile.get();
        return customFile.creator().getFile().delete();
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }

        return instance;
    }

    public Set<CustomFile> getCustomFiles() {
        return this.customFiles;
    }
}
