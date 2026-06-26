package com.codeberg.monsieurdoceo.filizer.storage.infrastructure;

import com.codeberg.monsieurdoceo.filizer.storage.domain.CustomFile;
import com.codeberg.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;

import java.util.Objects;

/**
 * Coordinates file synchronization by delegating the work
 * to the configured synchronization strategy.
 */
public final class FileSynchronizer {

    /**
     * The synchronization strategy to use.
     */
    private final FileSynchronizationStrategy strategy;

    /**
     * Creates a new synchronizer.
     *
     * @param strategy the synchronization strategy
     */
    public FileSynchronizer(FileSynchronizationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    /**
     * Ensures that the given file is synchronized.
     *
     * @param file the file to synchronize
     */
    public void ensureUpToDate(CustomFile file) { this.strategy.synchronize(file); }
}
