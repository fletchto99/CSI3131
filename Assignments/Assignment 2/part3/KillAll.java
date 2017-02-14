import java.rmi.RemoteException;

/**
 * The entrypoint for the application
 */
public class KillAll {
    public static void main(String... args) throws RemoteException {
        if (args.length < 1) {
            System.err.println("Usage: Killall <process>");
            return;
        }

        new Thread(new Server()).start();
        new Thread(new Client(args[0])).start();
    }
}
