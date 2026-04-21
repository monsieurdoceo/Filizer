package codeberg.monsieurdoceo.filizer;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor;

    public TestCommand() {
        this.fileAccessor = new FileAccessor();
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (args[0] == null || args[0].isEmpty()) {
            Bukkit.getLogger().warning(
                "[Filizer] The name cannot be null or empty."
            );
            return;
        }

        String fileName = args[0];
        FileGetter fileGetter = this.fileAccessor.Access(fileName);
        Bukkit.getLogger().info(
            "The file has for variable name: " + fileGetter.getString("name")
        );
    }
}
