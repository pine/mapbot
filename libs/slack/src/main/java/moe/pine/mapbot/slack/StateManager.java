package moe.pine.mapbot.slack;

interface StateManager {
    boolean isClosed();

    default void throwIfAlreadyClosed() {
        if (isClosed()) {
            throw new IllegalStateException("The server has already been shutdown.");
        }
    }
}
