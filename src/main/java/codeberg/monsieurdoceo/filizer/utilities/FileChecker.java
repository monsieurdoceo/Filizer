package codeberg.monsieurdoceo.filizer.utilities;

import java.nio.file.Files;
import java.nio.file.Path;
import org.bukkit.Bukkit;

/**
 * Utility methods for validating file names and checking file paths.
 */
public final class FileChecker {

    /**
    * Checks whether a file name is valid.
    *
    * <p>If the name is invalid, an error message is logged to the
    * Bukkit logger.
    *
    * @param name the file name to validate
    * @return {@code true} if the name is valid,
    * otherwise {@code false}
 */
    public static boolean hasValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            Bukkit.getLogger().severe(
                "[Filizer] The name of the file can't be null or empty."
            );
            return false;
        }

        return true;
    }

    /**
     * Checks whether the specified file exists.
     *
     * @param path the path to checks
     * @return {@code true} if the file exists,
     * otherwise {@code false}
*/
    public static boolean fileExists(Path path) {
        return Files.exists(path);
    }
}
