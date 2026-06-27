package io.github.monsieurdoceo.filizer.storage.infrastructure;

import io.github.monsieurdoceo.filizer.shared.exceptions.FilizerExceptions;
import io.github.monsieurdoceo.filizer.shared.util.FileChecker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Factory responsible for creating managed files.
 *
 * <p>Handles file name validation, directory creation,
 * and file initialization.
 */
public final class FileFactory {

    /**
     * Initializes the file factory.
     *
     * <p>No custom initialization is required.
     */
    public FileFactory() {}

    /**
     * Creates a file from a parent directory path and file name.
     *
     * <p>If the parent directories do not exist,
     * they are created automatically.
     *
     * @param path the parent directory path
     * @param name the file name
     * @param errors the exception factory
     * @return the created file
     * @throws IllegalArgumentException if the file name is invalid
     * @throws IllegalStateException if an I/O error occurs while creating the file
     */
    public static File createFile(
        final Path path,
        final String name,
        final FilizerExceptions errors
    ) {
        Objects.requireNonNull(errors, "errors");

        if (!FileChecker.hasValidName(name)) throw errors.invalidFileName(
            name,
            null
        );

        Path filePath = path.resolve(name);

        try {
            if (
                !FileChecker.exists(filePath.getParent())
            ) Files.createDirectories(filePath.getParent());
            if (!FileChecker.exists(filePath)) Files.createFile(filePath);
            return filePath.toFile();
        } catch (IOException e) {
            throw errors.fileCreationFailed(path, name, e);
        }
    }
}
