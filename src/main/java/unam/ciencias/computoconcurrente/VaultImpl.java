package unam.ciencias.computoconcurrente;

import java.util.Random;

/**
 *
 * @author ferfong
 */
public class VaultImpl implements Vault {
    
    private int password;
    private boolean passwordFound;
    int correctPassword;
    Lock lock;
    Random random;
    
    public VaultImpl() {
        this.lock = new PetersonLock(); 
        this.random = new Random();
    }

    @Override
    public void setPassword() {
        this.password = random.nextInt(Integer.MAX_VALUE);
    }

    @Override
    public boolean isCorrectPassword(int possiblePassword) {
        if(possiblePassword != this.password) {
            return false;
        } else {
            lock.lock();
            passwordFound = true;
            correctPassword = possiblePassword;
            lock.unlock();
        }
        return true;
    }

    @Override
    public boolean wasPasswordFound() {
        lock.lock();
        boolean lastSeen = passwordFound;
        lock.unlock();
        return lastSeen;
    }
    
}
