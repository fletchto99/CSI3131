import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

class Server extends UnicastRemoteObject implements ServerInterface, Runnable {

    private int port;
    private String appName;

    public Server() throws RemoteException {
        this(ServerInterface.DEFAULT_PORT, ServerInterface.APP_NAME);
    }

    public Server(int port, String appName) throws RemoteException {
        this.port = port;
        this.appName = appName;
    }

    /**
     * The main implementation which starts the server
     */
    public void run() {
        try {
            System.out.println("[Server] Starting " + appName +" port " + Integer.toString(port));
            Registry reg = LocateRegistry.createRegistry(port);
            reg.bind(appName, this);
        } catch (Exception e) {
            System.out.println("[Server] Error starting server!");
            e.printStackTrace();
        }

        System.out.println("[Server] Complete");
    }

    /**
     * Retrieve a list of all running processes
     */
    public List<String> listAll() throws IOException {
        System.out.println("[Server] Client called listAll()");
        BufferedReader output = new BufferedReader(new InputStreamReader(new ProcessBuilder("ps", "-eww", "-o", "pid=", "-o", "comm=").start().getInputStream(), "UTF-8"));
        return output.lines().parallel().collect(Collectors.toList());
    }
}
