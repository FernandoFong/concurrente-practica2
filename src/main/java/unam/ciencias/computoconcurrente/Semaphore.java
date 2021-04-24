package unam.ciencias.computoconcurrente;

public interface Semaphore {

    /**
     * Return how many threads are allowed to run the critical section at the same time
     * @return the number concurrent threads permitted to execute the critical section
     */
    int getPermitsOnCriticalSection();

    /**
     *
     */
    void acquire();


    /**
     *
     */
    void release();
}
