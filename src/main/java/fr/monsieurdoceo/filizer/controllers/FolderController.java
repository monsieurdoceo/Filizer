package fr.monsieurdoceo.filizer.controllers;

import com.google.common.collect.Sets;
import fr.monsieurdoceo.filizer.managers.FileManager;

import java.io.File;
import java.util.Optional;
import java.util.Set;

public final class FolderController {
    private final Set<File> files;
    private final FileManager fileManager;

    public FolderController(FileManager fileManager) {
        this.fileManager = fileManager;
        this.files = fileManager.getFiles();
    }

    public void loopFolder(File folder) {
        Set<File> fileList = getFolderListFiles(folder);
        if (fileList.isEmpty()) {
            return;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                loopFolder(file);
                continue;
            }

            this.fileManager.addFile(file, false);
        }
    }

    public Set<File> getFolderListFiles(File folder) {
        File[] fileList = folder.listFiles();
        return (fileList == null) ? Sets.newHashSet() : Sets.newHashSet(fileList);
    }

    public Optional<File> findFolderByName(String folderName) {
        return this.files.stream()
                .map(File::getParentFile)
                .filter(parentFile -> parentFile.getName().equalsIgnoreCase(folderName))
                .findFirst();
    }
}
