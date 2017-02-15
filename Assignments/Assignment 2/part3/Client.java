import java.io.*;
import java.net.Socket;

class Client implements Runnable {

    private String toKill;
    private int port;

    public Client(String toKill) {
        this(toKill, Server.DEFAULT_PORT);
    }

    public Client(String toKill, int port) {
        this.toKill = toKill;
        this.port = port;
    }


    public void run() {
        System.out.println("[Client] Connecting to localhost on port " + port);
        try {
            Socket socket = new Socket("127.0.0.1", port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            // Send LISTALL message to server
            System.out.println("[Client] Sending LISTALL message");
            out.println("LISTALL");

            // Read response from server
            long killed = in.lines()
                    .parallel() //exectute in parallel
                    .filter(line -> line.contains(toKill)) //remove any that don't meet our criteria
                    .map(line -> Integer.parseInt(line.trim().split("\\s+")[0])) //filter to pid only
                    .filter(pid -> {
                        System.out.println("[Client] Killing pid " + pid);
                        try {
                            return new ProcessBuilder("kill", Integer.toString(pid)).start().waitFor() == 0;
                        } catch (Exception e) {
                            System.out.println("[Client] Error killing pid " + pid);
                        }
                        return false;
                    }).count();
            System.out.println("[Client] Killed " + killed + " processes with the name " + toKill);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[Client] Complete");
    }
}
