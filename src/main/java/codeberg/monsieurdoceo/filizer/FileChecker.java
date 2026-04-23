package codeberg.monsieurdoceo.filizer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;

public final class FileChecker {

    public static boolean checkingIfFileNameCorrect(String name) {
        if (name == null || name.trim().isEmpty()) {
            Bukkit.getLogger().severe(
                "[Filizer]: The name of the file can't be null or empty."
            );
            return false;
        }

        return true;
    }

    public static void viewFileContents(
        FileAccessor fileAccessor,
        String name
    ) {
        FileGetter fileGetter = fileAccessor.Access(name);
        for (String key : fileGetter.getKeys(true)) {
            Object object = fileGetter.get(key);
            if (!(object instanceof MemorySection)) {
                Bukkit.getLogger().info(
                    "The file contain key: " +
                        key +
                        " with the value: " +
                        object
                );
            }
        }
    }
}
