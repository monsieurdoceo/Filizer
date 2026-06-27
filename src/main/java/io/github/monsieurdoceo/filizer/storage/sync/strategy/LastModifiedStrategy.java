package io.github.monsieurdoceo.filizer.storage.sync.strategy;

import io.github.monsieurdoceo.filizer.storage.domain.CustomFile;
import io.github.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;

/**
 * Synchronizes files using the filesystem last modified timestamp.
 */
public final class LastModifiedStrategy implements FileSynchronizationStrategy {

    /**
     * Creates a new {@link LastModifiedStrategy} instance.
     *
     * <p>This class does not require custom initialization.
     */
    public LastModifiedStrategy() {}

    /**
     * Synchronizes the given file if the last modified timestamp is newer.
     *
     * @param file the file to synchronize
     */
    @Override
    public void synchronize(CustomFile file) {
        if (
            file.getFile().lastModified() > file.getLastModified()
        ) file.reload();
    }
}
