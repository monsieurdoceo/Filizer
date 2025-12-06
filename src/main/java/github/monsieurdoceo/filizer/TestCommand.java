package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.FileBuilder;
import github.monsieurdoceo.filizer.FileCreator;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.io.File;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TestCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        if (args.length == 0) {
            Bukkit.getLogger().info(
                "The command need to asign a name to execute."
            );
            return;
        }

        String name = args[0];
        File file = new FileBuilder(
            new FileCreator("plugins/Filizer", name + ".yml")
        )
            .set("name", "Greg")
            .set("age", 22)
            .save();
    }
}
