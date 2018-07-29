package threading;

import java.util.Optional;
import java.util.concurrent.Semaphore;

public class TaskGroup {

    //double-buffered. Writing to one when reading from the other
    private Runnable[] ringBufferAlpha;
    private Runnable[] ringBufferBeta;

    private Semaphore readSemaphore = new Semaphore(1);
    private Semaphore writeSemaphore = new Semaphore(1);

    private int capacity = 0;
    private int writePosition = 0;
    private int available = 0;

    private int writePositionBeta = 0;
    private int availableBeta = 0;
    public TaskGroup(int capacity) {
        this.capacity = capacity;
        this.ringBufferAlpha = new Runnable[capacity];
        this.ringBufferBeta = new Runnable[capacity];
    }

    public void flip() {
        for (int i = 0; i < capacity; i++) {
            Runnable alphaClone = ringBufferAlpha[i];
            ringBufferAlpha[i] = ringBufferBeta[i];
            ringBufferBeta[i] = alphaClone;
        }

        //swap variables
        writePosition ^= writePositionBeta;
        writePositionBeta ^= writePosition;
        writePosition ^= writePositionBeta;

        available ^= availableBeta;
        availableBeta ^= available;
        available ^= availableBeta;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailable() {
        return available;
    }

    public void reset() {
        this.writePosition = 0;
        this.available = 0;
    }

    public boolean push(Runnable element) throws InterruptedException {
        if(element == null) {
            return false;
        }
        readSemaphore.acquire();
        if(availableBeta < capacity) {
            if(writePositionBeta >= capacity) {
                writePositionBeta = 0;
            }
            ringBufferBeta[writePositionBeta] = element;
            writePositionBeta++;
            availableBeta++;
            readSemaphore.release();
            return true;
        }
        readSemaphore.release();
        return false;
    }

    public Optional<Runnable> pop() throws InterruptedException {
        if(available == 0) {
            return Optional.empty();
        }
        writeSemaphore.acquire();
        int nextSlot = writePosition - available;
        if(nextSlot < 0) {
            nextSlot += capacity;
        }
        Runnable nextObject = ringBufferAlpha[nextSlot];
        available--;
        if(nextObject == null) {
            writeSemaphore.release();
            return Optional.empty();
        }
        writeSemaphore.release();
        return Optional.of(nextObject);
    }
}
