import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Application {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        //Saves the output to a file
        System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("Simulations.log"))));

        //Runs all of the threads
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.execute(new Simulation(0, 10, 3, 3, false));
        executor.execute(new Simulation(1, 10, 4, 4, false));
        executor.execute(new Simulation(2, 10, 3, 4, false));
        executor.execute(new Simulation(3, 10, 3, 3, true));
        executor.execute(new Simulation(4, 10, 4, 4, true));
        executor.execute(new Simulation(5, 10, 3, 4, true));

        // Wait for the simulations to complete then close the service
        executor.shutdown();
    }
}
