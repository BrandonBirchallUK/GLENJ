package threading;

public interface EventListener<T> {
    void onEvent(T event);
}
