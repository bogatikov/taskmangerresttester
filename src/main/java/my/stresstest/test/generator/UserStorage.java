package my.stresstest.test.generator;

import my.stresstest.test.data.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    public List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        File storage = new File("users");

        if (storage.listFiles() == null)
            return null;

        for (File f:
                storage.listFiles()) {
            users.add(loadUser(f));
        }
        return users;
    }

    public User loadUser(File file) {
        try {
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);

            User pr1 = (User) oi.readObject();

            oi.close();
            fi.close();
            return pr1;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(User user) {
        File storage = new File("users");
        storage.mkdir();
        try {
            FileOutputStream f = new FileOutputStream(new File("users/user-"+ user.getUsername() + ".txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(user);
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

}
