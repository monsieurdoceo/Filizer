package codeberg.monsieurdoceo.filizer;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor;

    public TestCommand() {
        this.fileAccessor = new FileAccessor();
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String fileName = args[0];
        FileGetter fileGetter = this.fileAccessor.Access(fileName);

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
