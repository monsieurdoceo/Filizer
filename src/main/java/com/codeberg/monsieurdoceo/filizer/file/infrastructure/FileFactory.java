package com.codeberg.monsieurdoceo.filizer.file.infrastructure;

import com.codeberg.monsieurdoceo.filizer.shared.util.FileChecker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileFactory {

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Creates a file from a parent directory path and file name.
     *
     * <p>If the parent directories do not exist,
     * they are created automatically.
     *
     * @param path the parent directory path
     * @param name the file name
     * @return the created file
     * @throws IllegalArgumentException if the file name is invalid
     * @throws IllegalStateException if an I/O error occurs while creating the file
     */
    public static File createFile(final Path path, final String name) {

        if(!FileChecker.hasValidName(name)) throw new IllegalArgumentException("[Filizer] Invalid file name: " + name);

        Path filePath = path.resolve(name);

        try {

            if(!FileChecker.exists(filePath.getParent())) Files.createDirectories(filePath.getParent());
            if(!FileChecker.exists(filePath)) Files.createFile(filePath);

            return filePath.toFile();

        } catch(IOException e) { throw new IllegalStateException("[Filizer] Critical I/O error for " + name, e); }
    }
}
