package codeberg.monsieurdoceo.filizer;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class TestCommand implements BasicCommand {

    private FileAccessor fileAccessor = new FileAccessor();
    private FileManager fileManager = new FileManager();

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        String fileName = args[0];
        if (
            this.fileManager.gFileStorage().findFilebyName(fileName).isEmpty()
        ) {
            this.fileManager.createFile("plugins/Filizer", fileName)
                .set("name", "Testo")
                .set("age", 30)
                .save();
        }

        FileChecker.viewFileContents(this.fileAccessor, fileName);
    }
}
