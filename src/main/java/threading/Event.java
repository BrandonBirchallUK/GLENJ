package threading;

public class Event {
    private final long timeOfCreation;

    public Event() {
        this.timeOfCreation = System.currentTimeMillis();
    }

    public final long getTimeOfCreation() {
        return timeOfCreation;
    }
}
