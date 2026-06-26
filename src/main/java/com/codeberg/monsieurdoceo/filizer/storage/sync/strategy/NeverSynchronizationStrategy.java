package com.codeberg.monsieurdoceo.filizer.storage.sync.strategy;
import com.codeberg.monsieurdoceo.filizer.storage.domain.CustomFile;
import com.codeberg.monsieurdoceo.filizer.storage.sync.FileSynchronizationStrategy;

/**
 * Never synchronizes files.
 *
 * <p>This strategy assumes that files cannot be modified
 * externally.
 */
public final class NeverSynchronizationStrategy implements FileSynchronizationStrategy {

    /**********************************************************/
    /********************** CONSTRUCTORS **********************/
    /**********************************************************/

    /**
     * Creates a new {@link NeverSynchronizationStrategy} instance.
     *
     * <p>This class does not require custom initialization.
     */
    public NeverSynchronizationStrategy() {}

    /*********************************************************/
    /*********************** FUNCTIONS ***********************/
    /*********************************************************/

    /**
     * Performs no synchronization.
     *
     * @param file ignored
     */
    @Override
    public void synchronize(CustomFile file) {/* Intentionally left blank. */}
}