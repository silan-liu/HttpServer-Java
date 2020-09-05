package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {

            }

            System.out.println("client disconnected");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

        // 读取 http 请求
        boolean requestOk = false;
        String first = reader.readLine();
        System.out.println("firstLine:" + first);
        if (first.startsWith("GET / HTTP/1.")) {
            requestOk = true;
        }

        for (;;) {
            String header = reader.readLine();
            if (header.isEmpty()) {
                break;
            }

            System.out.println(header);
        }

        if (!requestOk) {
            writer.write("HTTP/1.0 404");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        } else  {
            String data = "<html><body><h1>Hello</h1></body></html>";
            int length = data.getBytes(StandardCharsets.UTF_8).length;

            writer.write("HTTP/1.0 200");
            writer.write("Connection: close\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length " + length + "\r\n");
            writer.write("\r\n");
            writer.write(data);
            writer.flush();
        }
    }
}
