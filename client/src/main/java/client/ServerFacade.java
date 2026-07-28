package client;

import com.google.gson.Gson;
import server.request.CreateRequest;
import server.request.LoginRequest;
import server.request.RegisterRequest;
import server.result.CreateResult;
import server.result.LoginResult;
import server.result.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson = new Gson();
    private final int port;

    public ServerFacade(String url, int port) {
        System.out.println("Init ServerFacade");
        // Set up url to send http requests to
        serverUrl = url;
        this.port = port;
    }

    private String post(String path, String bodyString) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", serverUrl, port, path);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            return httpResponse.body();
        } else {
            System.out.println(gson.fromJson(httpResponse.body(), ErrorResult.class).message());
            return null;
        }
    }

    public LoginResult login(String username, String password) throws Exception {
        LoginRequest body = new LoginRequest(username, password);
        String bodyString = gson.toJson(body);
        String response = post("/session", bodyString);
        if (response == null) {
            return null;
        }
        return gson.fromJson(response, LoginResult.class);

    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        RegisterRequest body = new RegisterRequest(username, password, email);
        String bodyString = gson.toJson(body);
        String response = post("/user", bodyString);
        if (response == null) {
            return null;
        }
        return gson.fromJson(response, RegisterResult.class);
    }

    public void logout(String authToken) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", serverUrl, port, "/session");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .header("Authorization", authToken)
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println(gson.fromJson(httpResponse.body(), ErrorResult.class).message());
            throw new Exception("Logout failed");
        }
    }

    public int createGame(String name, String authToken) throws Exception {
        CreateRequest body = new CreateRequest(name);
        String bodyString = gson.toJson(body);
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", serverUrl, port, "/game");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .header("Authorization", authToken)
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            CreateResult result = gson.fromJson(httpResponse.body(), CreateResult.class);
            return result.gameID();
        } else {
            System.out.println(gson.fromJson(httpResponse.body(), ErrorResult.class).message());
            throw new Exception("Create game failed");
        }
    }

}
