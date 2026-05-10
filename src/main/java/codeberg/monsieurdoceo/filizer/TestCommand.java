package codeberg.monsieurdoceo.filizer;

import codeberg.monsieurdoceo.filizer.managers.FileAccessor;
import codeberg.monsieurdoceo.filizer.managers.FileManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor = new FileAccessor();
    private FileManager fileManager = new FileManager();

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String fileName = args[0];
        if (!(source.getSender() instanceof Player)) return;

        Player player = (Player) source.getSender();
        if (
            this.fileManager.getFileStorage().findFileByName(fileName).isEmpty()
        ) {
            this.fileManager.createFile("plugins/Filizer", fileName)
                .set("name", player.getName())
                .set("isOp", player.isOp())
                .save();
        }

        this.fileAccessor.access(fileName).ifPresent(fileGetter -> {
            for (String key : fileGetter.getKeys(true)) {
                Object object = fileGetter.get(key);
                if (!(object instanceof MemorySection)) {
                    player.sendMessage(
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
