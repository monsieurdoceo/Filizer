package codeberg.monsieurdoceo.filizer;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor;

    public TestCommand() {
        this.fileAccessor = new FileAccessor();
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String fileName = args[0];
        FileGetter fileGetter = this.fileAccessor.Access(fileName);

        if (fileGetter.has("ranks")) {
            for (String key : fileGetter.getKeys("ranks", false)) {
                ConfigurationSection rankSection = fileGetter.getSection(
                    "ranks." + key
                );
                int id = rankSection.getInt("id");
                Bukkit.getLogger().info(
                    "This file has a rank: " + key + " with the id: " + id
                );
            }
        }

        Bukkit.getLogger().info(
            "The file has for variable name: " + fileGetter.getString("name")
        );
    }
}
