package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.FileAccessor;
import github.monsieurdoceo.filizer.FileBuilder;
import github.monsieurdoceo.filizer.FileCreator;
import github.monsieurdoceo.filizer.FileSection;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.io.File;
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TestCommand implements BasicCommand {

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String name = args[0];
        FileCreator fileCreator = new FileCreator("plugins/Filizer", name);
        File file = new FileBuilder(fileCreator)
            .set("name", name.toUpperCase())
            .set("length", name.length())
            .list("groups", Arrays.asList("mama", "dada", "ratata"))
            .set(
                new FileSection("players")
                    .set(
                        new FileSection("mama")
                            .set("health", 20)
                            .set("stamina", 20)
                            .set("allowFlight", false)
                    )
                    .set(
                        new FileSection("dada")
                            .set("health", 15)
                            .set("stamina", 20)
                            .set("allowFlight", true)
                    )
                    .list("ratata", Arrays.asList("Mango", "Tomato"))
            )
            .save();

        FileAccessor fileAccessor = new FileAccessor(fileCreator);
        String fileName = fileAccessor.getString("name");
        int length = fileAccessor.getInt("length");
        source
            .getSender()
            .sendMessage(
                "The file " +
                    fileName +
                    " has been accessed and has a length of " +
                    length
            );
    }
}
