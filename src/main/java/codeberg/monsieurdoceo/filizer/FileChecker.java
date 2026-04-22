package codeberg.monsieurdoceo.filizer;

import org.bukkit.Bukkit;

public class FileChecker {

    public static boolean checkingIfFileNameCorrect(String name) {
        if (name == null || name.trim().isEmpty()) {
            Bukkit.getLogger().severe(
                "[Filizer]: The name of the file can't be null or empty."
            );
            return false;
        }

        return true;
    }
}
