import org.junit.Test;
import threading.Event;
import threading.EventListener;

public class EventsTest {
    @Test
    public void testEventSystem() {
        TestEvent eventA = new TestEvent("One");
        TestEvent eventB = new TestEvent("Two");

        EventListener<TestEvent> lister = event -> System.out.println(event.arg);

        lister.onEvent(eventA);
        lister.onEvent(eventB);
    }
}

class TestEvent extends Event {
    public String arg;

    public TestEvent(String args) {
        this.arg = args;
    }
}
