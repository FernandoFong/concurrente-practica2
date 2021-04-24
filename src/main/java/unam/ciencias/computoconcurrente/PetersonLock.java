package unam.ciencias.computoconcurrente;

import java.util.HashMap;
import java.util.Map;

/**
 * This particular implementation of a lock uses the Peterson's Algorithm which only work for two concurrent threads at
 * most.
 */
public class PetersonLock implements Lock {

    private boolean[] flag;
    private volatile int victim;

    public PetersonLock() {
        flag = new boolean[]{false, false};
    }

    @Override
    public void lock() {
        int threadId = this.getCurrentThreadId();

        flag[threadId] = true;
        victim = threadId;

        while(flag[1 - threadId] && victim==threadId) {}
    }

    @Override
    public void unlock() {
        int threadId = this.getCurrentThreadId();
        flag[threadId] = false;
    }

    private int getCurrentThreadId() {
        return Integer.parseInt(Thread.currentThread().getName()) % 2;
    }
}
