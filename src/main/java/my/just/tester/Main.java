package my.just.tester;

import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import my.just.tester.data.User;
import my.just.tester.generator.UserStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        Unirest.config().setObjectMapper(new JacksonObjectMapper());

/*        UserGenerator userGenerator = new UserGenerator();
        userGenerator.generate(100);*/

        UserStorage userStorage = new UserStorage();
        List<User> userList = userStorage.loadAllUsers();
        System.out.println(userList.size() + " users was loaded");

        Worker [] workers = new Worker[userList.size()/50 + 1];

        AtomicInteger counter = new AtomicInteger(0);

        int k = 0;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            if (i % 50 == 0) {
                workers[k++] = new Worker(users, counter);
                System.out.println("New worker with " + users.size() + " ready");
                users = new ArrayList<>();
            } else if (i == userList.size() - 1) {
                users.add(userList.get(i));
                workers[k++] = new Worker(users, counter);
                System.out.println("New worker with " + users.size() + " ready");
            }
            users.add(userList.get(i));
        }

        for (Worker w:
             workers) {
            new Thread(w).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Tasks send: " + counter.get());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
