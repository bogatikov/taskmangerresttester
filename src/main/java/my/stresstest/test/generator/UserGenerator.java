package my.stresstest.test.generator;

import my.stresstest.test.data.User;
import my.stresstest.test.request.Request;

import static my.stresstest.test.utils.Util.generateRandomString;

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
