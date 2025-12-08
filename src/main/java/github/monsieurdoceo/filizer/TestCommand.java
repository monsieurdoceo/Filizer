package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.FileBuilder;
import github.monsieurdoceo.filizer.FileCreator;
import github.monsieurdoceo.filizer.FileSection;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.io.File;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TestCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String name = args[0];
        int numberOfLoop = Integer.parseInt(args[1]);

        for (int i = 0; i < numberOfLoop; i++) {
            File file = new FileBuilder(
                new FileCreator("plugins/Filizer", name + "_" + i + ".yml")
            )
                .set("id", i)
                .set("name", name.toUpperCase())
                .set("length", name.length() * i)
                .list("groups", Arrays.asList("mama", "dada", "ratata"))
                .section(
                    new FileSection("players")
                        .set("health", 20)
                        .set("stamina", 20)
                        .set("allowFlight", false)
                )
                .save();
        }
    }
}
