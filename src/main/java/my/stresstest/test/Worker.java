package my.stresstest.test;

import my.stresstest.test.data.User;
import my.stresstest.test.request.Request;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {
    private List<User> users;
    private AtomicInteger counter;
    private int delay;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        while (counter.get() <= 15000) {
            try {
                for (User user:
                     users) {
                    Request.pushRandomTaskToUser(user);
                    counter.incrementAndGet();
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




    public Worker(List<User> users, AtomicInteger counter) {
        this.users = users;
        this.counter = counter;
    }


}
