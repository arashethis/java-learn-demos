package org.example.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(1000);
        ds.connect(InetAddress.getByName("localhost"), 6666);

        // Send data
        byte[] data = "Hello!".getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length);
        ds.send(packet);

        // Receive data
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        String s = new String(packet.getData(), packet.getOffset(), packet.getLength());
        System.out.println("[server] " + s);

        ds.disconnect();
        ds.close();
    }
}
