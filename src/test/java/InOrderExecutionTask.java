import org.junit.Test;
import threading.LinkedTask;
import threading.ThreadManager;

public class InOrderExecutionTask {
    @Test
    public void testInOrder() {
        Runnable r = () -> System.out.println("!");
        LinkedTask two = new LinkedTask(r) {
            @Override
            public void run() {
                System.out.println("World");
                super.run();
            }
        };
        LinkedTask one = new LinkedTask(two) {
            @Override
            public void run() {
                System.out.println("Hello,");
                super.run();
            }
        };
        ThreadManager.getInstance(1).submitTask(one);
        ThreadManager.getInstance().cleanUp();
    }
}
