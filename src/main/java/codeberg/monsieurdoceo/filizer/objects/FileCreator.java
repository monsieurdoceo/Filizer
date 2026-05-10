package codeberg.monsieurdoceo.filizer.objects;

import codeberg.monsieurdoceo.filizer.utilities.FileChecker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.Bukkit;

public final class FileCreator {

    private File file;

    private void createFile(Path path, String name) {
        if (!FileChecker.hasValidName(name)) return;

        Path filePath = path.resolve(name);
        try {
            if (
                !FileChecker.checkIfFileExist(filePath.getParent())
            ) Files.createDirectories(filePath.getParent());
            if (!FileChecker.checkIfFileExist(filePath)) Files.createFile(
                filePath
            );

            this.file = filePath.toFile();
        } catch (IOException e) {
            Bukkit.getLogger().severe(
                "[Filizer] Critical I/O error for " +
                    name +
                    ": " +
                    e.getMessage()
            );
        }
    }

    public FileCreator(Path path, String name) {
        createFile(path, name);
    }

    public FileCreator(File parent, String name) {
        createFile(parent.toPath(), name);
    }

    public File getFile() {
        return this.file;
    }
}
