package org.example.tcp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666);
        System.out.println("Server is running...");

        for(;;) {
            Socket sock = ss.accept();
            System.out.println("Connection from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try(OutputStream output = this.sock.getOutputStream()) {
                handler(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {}
            System.out.println("Client disconnected.");
        }
    }

    private void handler(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("Hello!\n");
        writer.flush();
        for (;;) {
            String s = reader.readLine();
            if(s.equals("exit")) {
                writer.write("exit\n");
                writer.flush();
                break;
            }

            System.out.println("[client] " + s);
            writer.write("Received: " + s + "\n");
            writer.flush();
        }
    }
}