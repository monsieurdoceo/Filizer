package io.github.monsieurdoceo.filizer.shared.exceptions;

import io.github.monsieurdoceo.filizer.shared.logging.AppLogger;
import java.util.Objects;

/**
 * Thrown when a file cannot be deleted.
 */
public final class FilizerFileDeletionException extends IllegalStateException {

    /**
     * Creates a new file-deletion exception.
     *
     * @param logger the application logger
     * @param name the file name
     * @param cause the original cause
     */
    public FilizerFileDeletionException(
        AppLogger logger,
        String name,
        Throwable cause
    ) {
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
    private static String message(
        AppLogger logger,
        String name,
        Throwable cause
    ) {
        logger.error(
            "IO Error deleting " + name + ": " + cause.getMessage(),
            cause
        );
        return logger.getCurrentlyLoggedMessage();
    }
}
