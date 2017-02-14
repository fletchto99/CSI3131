import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Server implements Runnable {
    public static int DEFAULT_PORT = 8080;

    private int port;

    public Server() {
        this(DEFAULT_PORT);
    }

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("[Server] Starting on port " + port);
        try {
            Socket client = new ServerSocket(port).accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("LISTALL")) {
                    try {
                        System.out.println("[Server] Received LISTALL message");
                        listAll(out);
                    } catch (IOException e) {
                        out.println("[Server] error listing processes");
                        System.out.println("[Server] Error sending response!");
                    } finally {
                        out.close();
                        in.close();
                    }
                    break;
                } else {
                    out.println("[Server] Invalid message: " + message);
                }
            }

        } catch (IOException e) {
            System.out.println("[Server] An unexpected error has occurred");
        }
        System.out.println("[Server] Finished");
    }

    /**
     * Query the operating system for the active list of processes
     */
    private void listAll(PrintWriter out) throws IOException {
        BufferedReader output = new BufferedReader(new InputStreamReader(new ProcessBuilder("ps", "-eww", "-o", "pid=", "-o", "comm=").start().getInputStream(), "UTF-8"));
        output.lines().parallel().forEach(out::println);
    }
}
