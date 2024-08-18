package org.example.http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 使用 curl 命令测试 curl -i localhost:8080
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("Server is running...");
        for (; ; ) {
            Socket s = ss.accept();
            System.out.println("Accepted connection from " + s.getRemoteSocketAddress());
            Thread t = new Handler(s);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) throws IOException {
        this.sock = sock;
    }

    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                this.handler(input, output);
            }
        } catch (IOException e) {
        } finally {
            try {
                this.sock.close();
            } catch (IOException e) {
            }
            System.out.println("Disconnected");
        }
    }

    private void handler(InputStream input, OutputStream output) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        boolean isRequestOK = false;

        String firstLine = reader.readLine();
        if (firstLine.startsWith("GET / HTTP/")) {
            isRequestOK = true;
        }
        for (; ; ) {
            String header = reader.readLine();
            if (header.isEmpty()) {
                break;
            }
            System.out.println(header);
        }
        System.out.println(isRequestOK ? "Response OK" : "Response NOT OK");

        if (!isRequestOK) {
            writer.write("HTTP/1.1 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        } else {
            String data = "<html><body><h1>Hello, world!</h1></body></html>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;
            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("Connection: close\r\n");
            writer.write("\r\n");
            writer.write(data);
            writer.flush();
        }
    }
}
