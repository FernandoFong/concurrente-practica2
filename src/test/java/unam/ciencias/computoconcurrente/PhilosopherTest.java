package unam.ciencias.computoconcurrente;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PhilosopherTest {
    static int TABLE_SIZE = 5;
    static int TEN_SECONDS = 10000;
    static int EXPECTED_EATING_TIMES = 500;

    Philosopher[] philosophers;
    Thread[] threads;
    Chopstick[] chopsticks;

    @BeforeEach
    void setUp() {
        initChopsticks();
    }

    void initChopsticks() {
        chopsticks = new Chopstick[TABLE_SIZE];
        for(int i = 0; i < TABLE_SIZE; i++) {
            chopsticks[i] = new ChopstickImpl(i);
        }
    }

    @Test
    void atLeastAllTriedToEatFilterAlgorithm()
            throws InterruptedException, InstantiationException, IllegalAccessException {
        initPhilosophersAndThreads(PhilosopherWithFilterAlgorithm.class);

        boolean allChopsticksWereTakenAtLeastOnce = true;
        for(Chopstick c : chopsticks) {
            allChopsticksWereTakenAtLeastOnce = allChopsticksWereTakenAtLeastOnce && c.getTimesTaken() > 0;
        }

        // At least all chopsticks were taken at least once
        assertTrue(allChopsticksWereTakenAtLeastOnce);
    }

    @Test
    void atLeastAllEatSeveralTimesFilterAlgorithm()
            throws InterruptedException, InstantiationException, IllegalAccessException {
        initPhilosophersAndThreads(PhilosopherWithFilterAlgorithm.class);

        boolean allEatEnoughTimes = true;
        for(Philosopher p : philosophers) {
            allEatEnoughTimes = allEatEnoughTimes && (p.getEatingCount() >= EXPECTED_EATING_TIMES &&
                    p.getEatingCount() <= p.getLeftChopstick().getTimesTaken() &&
                    p.getEatingCount() <= p.getRightChopstick().getTimesTaken());
        }

        // At least all philosophers ate enough times
        assertTrue(allEatEnoughTimes);
    }

    void initPhilosophersAndThreads(Class<? extends Philosopher> clazz) throws InterruptedException, InstantiationException, IllegalAccessException {
        initPhilosophers(clazz);
        initThreads();
        runThreads();
    }

    void initPhilosophers(Class<? extends Philosopher> clazz) throws InstantiationException, IllegalAccessException {
        philosophers = new Philosopher[TABLE_SIZE];
        for(int i = 0; i < TABLE_SIZE; i++) {
            philosophers[i] = clazz.newInstance();
            philosophers[i].setId(i);
            philosophers[i].setLeftChopstick(chopsticks[i]);
            philosophers[i].setRightChopstick(chopsticks[(i+1) % TABLE_SIZE]);
        }
    }

    void initThreads() {
        threads = new Thread[5];
        for(int i = 0; i < TABLE_SIZE; i++) {
            threads[i] = new Thread(philosophers[i], String.format("%d", i));
        }
    }

    void runThreads() throws InterruptedException {
        for(Thread thread: threads) {
            thread.start();
        }

        Thread.sleep(TEN_SECONDS);

        for(Thread t : threads) {
            t.interrupt();
        }

        for(Thread t : threads) {
            t.join();
        }
    }
}
