package codeberg.monsieurdoceo.filizer;

import codeberg.monsieurdoceo.filizer.managers.FileAccessor;
import codeberg.monsieurdoceo.filizer.managers.FileManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor = new FileAccessor();
    private FileManager fileManager = new FileManager();

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String fileName = args[0];
        if (
            this.fileManager.getFileStorage().findFilebyName(fileName).isEmpty()
        ) {
            this.fileManager.createFile("plugins/Filizer", fileName)
                .set("name", "Testo")
                .set("age", 30)
                .save();
        }

        this.fileAccessor.access(fileName).ifPresent(fileGetter -> {
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
        });
    }
}
