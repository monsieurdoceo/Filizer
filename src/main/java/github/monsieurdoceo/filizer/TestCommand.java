package github.monsieurdoceo.filizer;

import github.monsieurdoceo.filizer.CustomFile;
import github.monsieurdoceo.filizer.FileAccessor;
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
        CustomFile customFile = new CustomFile("plugins/Filizer", name);
        customFile
            .builder()
            .set("name", name.toUpperCase())
            .set("length", name.length());
        customFile
            .builder()
            .list("groups", Arrays.asList("mama", "dada", "ratata"));
        customFile
            .builder()
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
            );
        customFile.builder().save();

        FileAccessor fileAccessor = new FileAccessor(customFile.creator());
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
