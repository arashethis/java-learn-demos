package org.example.http;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Client {
    static HttpClient client = HttpClient.newBuilder().build();

    public static void main(String[] args) throws Exception {
        String url = "https://www.sina.com.cn";

        HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .header("User-Agent", "Mozilla/5.0")
                .header("Accept", "*/*")
                .timeout(Duration.ofSeconds(7))
                .version(HttpClient.Version.HTTP_2)
                .build();

        System.out.println("\n------- read as string --------");
        readAsString(request);

        System.out.println("\n------- read as input stream --------");
        readAsInputStream(request);

        System.out.println("\n------- send request --------");
        sendPostRequest();
    }

    public static void readAsString(HttpRequest request) throws Exception {
        HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, List<String>> headers = resp.headers().map();
        for (String key : headers.keySet()) {
            System.out.println(key + ": " + headers.get(key).get(0));
        }

        System.out.println(resp.body().substring(0, 128) + "...");
    }

    public static void readAsInputStream(HttpRequest request) throws Exception {
        HttpResponse<InputStream> resp = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        Map<String, List<String>> headers = resp.headers().map();
        for (String key : headers.keySet()) {
            System.out.println(key + ": " + headers.get(key).get(0));
        }

        int n;
        StringBuilder sb = new StringBuilder();
        while ((n = resp.body().read()) != -1) {
            sb.append((char) n);
        }
        System.out.println(sb.substring(0, 128) + "...");
    }

    public static void sendPostRequest() throws Exception {
        String url = "https://www.sina.com.cn";
        String body = "username=test&password=123456";

        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(url))
                .header("Accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resp = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String s = resp.body();
        System.out.println(s);
    }
}
