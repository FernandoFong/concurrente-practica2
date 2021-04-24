package unam.ciencias.computoconcurrente;

/**
 *
 * @author ferfong
 */
public class ThiefImpl implements Thief {
    
    @Override
    public boolean findPassword(Vault vault, int lowerBound, int upperBound) {
        for(int i = lowerBound; i <= upperBound; i++) {
            if(vault.isCorrectPassword(i)) {
                return true;
            }
        }
        return false;
    }
}
