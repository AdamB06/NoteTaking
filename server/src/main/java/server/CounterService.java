package server;

import org.springframework.stereotype.Service;

@Service
public class CounterService {
    private int count = 0;

    /**
     * @return the current count and increases it by one
     */
    public int getAndIncrease(){
        return count++;
    }
}
