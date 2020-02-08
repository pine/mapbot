package moe.pine.mapbot.slack;

@FunctionalInterface
public interface EventListener {
    void accept(Event t) throws InterruptedException;
}
