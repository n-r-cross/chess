package client;

import com.google.gson.Gson;
import server.request.LoginRequest;
import server.request.RegisterRequest;
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
    private final int port;

    private String authToken = "";

    public ServerFacade(String url, int port) {
        System.out.println("Init ServerFacade");
        // Set up url to send http requests to
        serverUrl = url;
        this.port = port;
    }

    public boolean loggedIn() {
        return authToken.isEmpty();
    }

    public LoginResult login(String username, String password) throws Exception {
        System.out.println("Running login in Server Facade");
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", serverUrl, port, "/session");

        LoginRequest body = new LoginRequest(username, password);
        String bodyString = new Gson().toJson(body);

        System.out.println("Building request");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ran request");
        System.out.println(httpResponse.body());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
        // TODO: save authToken
        return new LoginResult("ga", "ga");
    }

    public RegisterResult register(String username, String password, String email) throws Exception {
        System.out.println("Running register in Server Facade");
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", serverUrl, port, "/user");

        RegisterRequest body = new RegisterRequest(username, password, email);
        String bodyString = new Gson().toJson(body);

        System.out.println("Building request");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Ran request");
        System.out.println(httpResponse.body());
        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
        // TODO: save authToken
        return new RegisterResult("ga", "ga");
    }

}
