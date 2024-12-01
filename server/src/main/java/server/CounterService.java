package server;

import org.springframework.stereotype.Service;

@Service
public class CounterService {
    private int count = 0;
    public int getAndIncrease(){
        return count++;
    }
}
