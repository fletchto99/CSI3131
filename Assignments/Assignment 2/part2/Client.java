import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class Client implements Runnable {

    private String toKill;
    private int port;

    public Client(String toKill) {
        this.toKill = toKill;
        this.port = ServerInterface.DEFAULT_PORT;
    }

    public Client(String toKill, int port) {
        this.toKill = toKill;
        this.port = port;
    }

    public void run() {
        System.out.println("[Client] Connecting to localhost:" + Integer.toString(port));
        try {
            System.out.println("[Client] Calling listAll() method");
            Registry registry = LocateRegistry.getRegistry("localhost", port);
            ServerInterface server = (ServerInterface)registry.lookup(ServerInterface.APP_NAME);

            // Read response from server
            long killed = server.listAll()
                    .parallelStream() //exectute in parallel
                    .filter(line -> line.contains(toKill)) //remove any that don't meet our criteria
                    .map(line -> Integer.parseInt(line.trim().split("\\s+")[0])) //filter to pid only
                    .peek(pid -> {
                        System.out.println("[Client] Killing pid " + pid);
                        try {
                            new ProcessBuilder("kill", Integer.toString(pid)).start();
                        } catch (IOException e) {
                            System.out.println("[Client] Error killing pid " + pid);
                        }
                    }).count();
            System.out.println("[Client] Attempted to kill " + killed + " processes with the name " + toKill);
        } catch (Exception e) {
            System.out.println("[Client] An error has occurred");
        }
        System.out.println("[Client] Finished killing processes");
    }

}
