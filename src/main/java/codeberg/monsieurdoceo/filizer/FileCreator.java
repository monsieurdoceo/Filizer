package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.Bukkit;

public class FileCreator {

    private File file;

    private void createFile(File parent, String name) {
        if (!FileChecker.checkingIfFileNameCorrect(name)) return;

        Path filePath = parent.toPath().resolve(name);
        try {
            if (Files.exists(filePath.getParent())) Files.createDirectories(
                filePath.getParent()
            );
            if (Files.notExists(filePath)) Files.createFile(filePath);

            this.file = filePath.toFile();
        } catch (IOException e) {
            Bukkit.getLogger().severe(
                "[Filizer]: Critical I/O error for " +
                    name +
                    ": " +
                    e.getMessage()
            );
        }
    }

    public FileCreator(String path, String name) {
        File parent = new File(path);
        createFile(parent, name);
    }

    public FileCreator(File parent, String name) {
        createFile(parent, name);
    }

    public File getFile() {
        return this.file;
    }
}
