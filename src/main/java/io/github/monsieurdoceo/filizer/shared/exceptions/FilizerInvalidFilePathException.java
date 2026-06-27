package io.github.monsieurdoceo.filizer.shared.exceptions;

import io.github.monsieurdoceo.filizer.shared.logging.AppLogger;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Thrown when a file path or file name is invalid.
 */
public final class FilizerInvalidFilePathException
    extends IllegalArgumentException
{

    /**
     * Creates a new invalid-file-path exception.
     *
     * @param logger the application logger
     * @param path the file path
     * @param name the file name
     * @param cause the original cause
     */
    public FilizerInvalidFilePathException(
        AppLogger logger,
        Path path,
        String name,
        Throwable cause
    ) {
        super(
            message(
                Objects.requireNonNull(logger, "logger"),
                path,
                name,
                cause
            ),
            cause
        );
    }

    /**
     * Creates a message for the exception.
     *
     * @param logger the application logger
     * @param path   the file path
     * @param cause  the original cause
     * @return the exception message
     */
    private static String message(
        AppLogger logger,
        Path path,
        String name,
        Throwable cause
    ) {
        logger.error("Invalid file path or name: " + path + "/" + name, cause);
        return logger.getCurrentlyLoggedMessage();
    }
}
