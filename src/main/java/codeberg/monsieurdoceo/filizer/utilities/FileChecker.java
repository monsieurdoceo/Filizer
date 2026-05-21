package codeberg.monsieurdoceo.filizer.utilities;

import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.Bukkit;

public final class FileChecker {

    public static boolean hasValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            Bukkit.getLogger().severe(
                "[Filizer] The name of the file can't be null or empty."
            );
            return false;
        }

        return true;
    }

    public static boolean checkIfFileExists(Path path) {
        return Files.exists(path);
    }
}
