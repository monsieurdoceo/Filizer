package com.codeberg.monsieurdoceo.filizer.commands;

import com.codeberg.monsieurdoceo.filizer.storage.api.FileManager;
import com.codeberg.monsieurdoceo.filizer.storage.infrastructure.FileReader;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

public class TestCommand implements BasicCommand, CommandExecutor {

    /**********************************************************/
    /*********************** PROPERTIES ***********************/
    /**********************************************************/

    private final FileManager fileManager;

    /*********************************************************/
    /********************** CONSTRUCTOR **********************/
    /*********************************************************/

    public TestCommand(FileManager fileManager) { this.fileManager = fileManager; }

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Executes the command.
     * @param source the source of the command
     * @param args the arguments of the command ignoring repeated spaces
     */
    @Override
    public void execute(CommandSourceStack source, String[] args) { handle(source.getSender(), args); }

    /**
     * Called when a command is executed
     *
     * @param sender Source of the command
     * @param command Command that was executed
     * @param label Alias of the command that was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        handle(sender, args);
        return true;
    }

    /**
     * Handle the command
     * @param sender the sender of the command
     * @param args the arguments of the command
     */
    private void handle(CommandSender sender, String[] args) {

        if(args.length == 0) {
            sender.sendMessage("Usage: /check <file>");
            return;
        }

        String fileName = args[0];

        this.fileManager.findFile(fileName).ifPresent(customFile -> {

            FileReader reader = customFile.getFileReader();

            for(String key : reader.getKeys(true)) {

                Object object = reader.get(key);

                if(!(object instanceof MemorySection))
                    sender.sendMessage("The file contain key: " + key + " with the value: " + object);
            }
        });
    }
}
