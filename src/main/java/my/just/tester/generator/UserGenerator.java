package my.just.tester.generator;

import my.just.tester.data.User;
import my.just.tester.request.Request;

import static my.just.tester.utils.Util.generateRandomString;

public class UserGenerator {
    private final UserStorage userStorage;

    public UserGenerator() {
        userStorage = new UserStorage();
    }

    public void generate(int usersCount) {

        for (int i = 0; i < usersCount; i++) {
            User user = new User();
            user.setUsername(generateRandomString(5));
            user.setPassword(generateRandomString(3));
            user.setFirstName(generateRandomString(10));
            user.setLastName(generateRandomString(10));
            user.setEmail(generateRandomString(5) + "@" + generateRandomString(4) + ".com");
            Request.register(user);
            Request.login(user);
            userStorage.save(user);
        }
    }

}
