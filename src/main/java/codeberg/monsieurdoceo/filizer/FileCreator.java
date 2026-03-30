package codeberg.monsieurdoceo.filizer;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;

public class FileCreator {

    private File file;

    private boolean checkingIfParentFileExists(File parent) {
        if (parent.exists()) return true;
        if (!parent.mkdirs()) {
            Bukkit.getLogger().severe(
                "[Filizer]: Error when creating the folder parent."
            );
            return false;
        }

        return true;
    }

    private boolean checkingIfFileNameCorrect(String name) {
        if (name == null || name.isEmpty()) {
            Bukkit.getLogger().severe(
                "[Filizer]: The name of the file can't be null or empty."
            );
            return false;
        }

        return true;
    }

    private void createFile(File parent, String name) {
        if (!checkingIfParentFileExists(parent)) return;
        if (!checkingIfFileNameCorrect(name)) return;

        File file = new File(parent, name);
        try {
            file.createNewFile();
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
