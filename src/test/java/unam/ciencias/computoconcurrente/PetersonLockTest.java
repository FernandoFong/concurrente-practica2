package unam.ciencias.computoconcurrente;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PetersonLockTest {
    static final int ITERATIONS = 100;
    static final int MAX_VALUE = 500000;
    Lock lock;
    Counter counter;

    @BeforeEach
    void setUp() {
        lock = new PetersonLock();
    }

    @Test
    void lock() throws InterruptedException {
        Thread[] threads = new Thread[2];
        for(int i = 0; i < ITERATIONS; i++) {
            counter = new Counter(lock);
            threads[0] = new Thread(this::incrementCounter, "0");
            threads[1] = new Thread(this::incrementCounter, "1");
            threads[0].start();
            threads[1].start();
            threads[0].join();
            threads[1].join();

            assertEquals(2 * MAX_VALUE, counter.getValue());
        }
    }

    void incrementCounter() {
        for(int i = 0; i < MAX_VALUE; i++) {
            counter.getAndIncrement();
        }
    }
}

class Counter {
    private volatile int value;
    private Lock lock;

    Counter(Lock lock) {
        this.value = 0;
        this.lock = lock;
    }
    int getAndIncrement() {
        this.lock.lock();
        int result = this.value;
        this.value = this.value + 1;
        this.lock.unlock();
        return result;
    }

    int getValue() {
        return value;
    }
}