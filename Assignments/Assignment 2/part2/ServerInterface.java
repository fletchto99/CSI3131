import java.io.IOException;
import java.rmi.Remote;
import java.util.List;

/**
 * An interface for the server to implement
 */
interface ServerInterface extends Remote {

    //Some default settings for the app
    int DEFAULT_PORT = 8080;
    String APP_NAME = "KillAllApp";

    List<String> listAll() throws IOException;
}
