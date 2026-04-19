package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;

public class FileCreator {

    private File file;

    private void createFile(File parent, String name) {
        if (!FileChecker.checkingIfParentFileExists(parent)) return;
        if (!FileChecker.checkingIfFileNameCorrect(name)) return;

        this.file = new File(parent, name);
        try {
            this.file.createNewFile();
        } catch (IOException ioException) {
            Bukkit.getLogger().severe(
                "[Filizer]: Error when creating the file: " + ioException
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
