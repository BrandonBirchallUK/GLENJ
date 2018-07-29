package threading;

public abstract class LinkedTask implements Runnable {

    private final Runnable nextTask;

    public LinkedTask(Runnable nextTask) {
        this.nextTask = nextTask;
    }

    @Override
    public void run() {
        nextTask.run();
    }
}
