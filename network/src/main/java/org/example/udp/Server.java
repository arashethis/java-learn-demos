package org.example.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket(6666);

        for (; ; ) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            ds.receive(packet);
            String s = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.println("[client] " + s);

            byte[] data = "ACK".getBytes();
            packet.setData(data);
            ds.send(packet);
        }
    }
}
