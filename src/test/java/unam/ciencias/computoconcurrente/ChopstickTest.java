package unam.ciencias.computoconcurrente;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ChopstickTest {
    static int ROUNDS = 300;
    StringBuffer buffer;
    Chopstick chopstick;
    Thread[] threads;

    @BeforeEach
    void setUp() {
        chopstick = new ChopstickImpl(1);
        buffer =  new StringBuffer();
        threads = new Thread[2];
        threads[0] = new Thread(this::takeAndReleaseChopStick, String.format("%d", generateRandomEvenNumber()));
        threads[1] = new Thread(this::takeAndReleaseChopStick, String.format("%d", generateRandomOddNumber()));
    }

    int generateRandomOddNumber() {
        return generateRandomEvenNumber() +  1;
    }

    int generateRandomEvenNumber() {
        return 2 * new Double(Math.random() * 1000).intValue();
    }

    @Test
    void twoThreads() throws InterruptedException {
        threads[0].start();
        threads[1].start();
        threads[0].join();
        threads[1].join();
        assertTrue(buffer.toString().matches("^(ab|cd)+$"));
        assertEquals(2 * ROUNDS, chopstick.getTimesTaken());
    }


    void takeAndReleaseChopStick() {
        for(int i = 0; i < ROUNDS; i++) {
            chopstick.take();
            Thread t = Thread.currentThread();
            boolean isEven = Integer.parseInt(t.getName()) % 2 == 0;
            buffer.append(isEven ? 'a' : 'c');
            buffer.append(isEven ? 'b' : 'd');
            chopstick.release();
        }
    }
}
