package my.stresstest.test.request;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import my.stresstest.test.data.Task;
import my.stresstest.test.data.User;
import my.stresstest.test.generator.UserGenerator;
import my.stresstest.test.utils.Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Request {
    public static void register(User user) {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/api/v1/auth/signup")
                .header("Content-Type", "application/json")
                .body(user)
                .asJson();

        user.setId(Integer.toUnsignedLong((int)response.getBody().getObject().get("id")));
    }

    public static void login(User user) {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/api/v1/auth/signin")
                .header("Content-Type", "application/json")
                .body(new AuthRequest(user.getUsername(), user.getPassword()))
                .asJson();

        user.setAccessToken((String) response.getBody().getObject().get("accessToken"));
        user.setRefreshToken((String) response.getBody().getObject().get("refreshToken"));
    }

    public static void refreshToken(User user) {
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/api/v1/auth/refreshToken")
                .header("Content-Type", "application/json")
                .body(new HashMap<String, String>().put("refreshToken", user.getRefreshToken()))
                .asJson();

        System.out.println("Refresh token for " + user.getUsername());
        user.setAccessToken((String) response.getBody().getObject().get("accessToken"));
        user.setRefreshToken((String) response.getBody().getObject().get("refreshToken"));
    }

    public static void pushRandomTaskToUser(User user) {
        Task task = new Task();
        task.setTitle(Util.generateRandomString(new Random().nextInt(100)));
        task.setDescription(Util.generateRandomString(new Random().nextInt(200)));
        task.setDone(true);
        task.setTargetDate(new Date());
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:8080/api/v1/tasks")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer_" + user.getAccessToken())
                .body(task)
                .asJson()
                .ifFailure(rsp -> {
                    rsp.getParsingError().ifPresent(e -> {
                        System.out.println("Parsing Exception: " + e.getOriginalBody());
                        UserGenerator userGenerator = new UserGenerator();
                        Request.refreshToken(user);
                    });
                });
    }
}
