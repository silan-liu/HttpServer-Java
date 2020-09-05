package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
	    // write your code here
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("server started");

        for (;;) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());

            Thread t = new Handler(sock);
            t.start();
        }
    }
}
