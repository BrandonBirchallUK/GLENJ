import org.junit.Test;
import threading.TaskGroup;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskGroupTest {
    @Test
    public void testAddTask() throws InterruptedException {
        TaskGroup tg = new TaskGroup(8);

        Runnable x = () -> {
            System.out.println("Hello");
        };
        Runnable y = () -> {
            System.out.println("I");
        };
        Runnable z = () -> {
            System.out.println("am");
        };
        Runnable a = () -> {
            System.out.println("In order");
        };
        Runnable b = () -> {
            System.out.println("not");
        };

        Runnable[] array = new Runnable[] {x,y,z,a,b};

        tg.push(x);
        tg.push(y);
        tg.push(z);
        tg.push(a);

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        tg.flip();

        for (int i = 0; i < 5; i++) {
            tg.push(array[i]);
        }

        for (int i = 0; i < 4; i++) {
            Optional<Runnable> taskOptional = tg.pop();
            taskOptional.ifPresent(Runnable::run);
        }

        tg.flip();

        for (int i = 0; i < 5; i++) {
            Optional<Runnable> taskOptional = tg.pop();
            taskOptional.ifPresent(threadPool::submit);
        }
        threadPool.shutdown();
    }
}
