package codeberg.monsieurdoceo.filizer;

import java.io.File;
import org.bukkit.Bukkit;

public class FileChecker {

    public static boolean checkingIfParentFileExists(File parent) {
        if (parent.exists()) return true;
        if (!parent.mkdirs()) {
            Bukkit.getLogger().severe(
                "[Filizer]: Error when creating the folder parent."
            );
            return false;
        }

        return true;
    }

    public static boolean checkingIfFileNameCorrect(String name) {
        if (name == null || name.isEmpty()) {
            Bukkit.getLogger().severe(
                "[Filizer]: The name of the file can't be null or empty."
            );
            return false;
        }

        return true;
    }
}
