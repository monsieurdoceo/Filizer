package codeberg.monsieurdoceo.filizer;

import codeberg.monsieurdoceo.filizer.managers.FileManager;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;

public final class FilizerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        FileManager fileManager = new FileManager();
        try {
            fileManager.storeAllFiles(getDataPath());
        } catch (IOException e) {
            throw new RuntimeException(
                "Error couldn't store all files from the dataFolder",
                e
            );
        }

        registerCommand("check", new TestCommand());
    }
}
