package com.codeberg.monsieurdoceo.filizer.shared.exceptions;

import com.codeberg.monsieurdoceo.filizer.shared.logging.AppLogger;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Thrown when a file cannot be created.
 */
public final class FilizerFileCreationException extends IllegalStateException {

    /**
     * Creates a new file-creation exception.
     *
     * @param logger the application logger
     * @param path the file path
     * @param name the file name
     * @param cause the original cause
     */
    public FilizerFileCreationException(AppLogger logger, Path path, String name, Throwable cause) {
        super(message(Objects.requireNonNull(logger, "logger"), name, cause));
    }

    /**
     * Creates a message for the exception.
     *
     * @param logger the application logger
     * @param name the file name
     * @param cause the original cause
     * @return the exception message
     */
    private static String message(AppLogger logger, String name, Throwable cause) {
        logger.error("Critical I/O error for " + name, cause);
        return logger.getCurrentlyLoggedMessage();
    }
}
