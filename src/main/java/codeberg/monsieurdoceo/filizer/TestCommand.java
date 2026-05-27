package codeberg.monsieurdoceo.filizer;

import codeberg.monsieurdoceo.filizer.managers.FileAccessor;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.configuration.MemorySection;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor = new FileAccessor();

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String fileName = args[0];
        this.fileAccessor.access(fileName).ifPresent(fileGetter -> {
            for (String key : fileGetter.getKeys(true)) {
                Object object = fileGetter.get(key);
                if (!(object instanceof MemorySection)) {
                    source.getSender().sendMessage(
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
