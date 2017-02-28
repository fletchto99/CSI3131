import java.util.concurrent.Semaphore;

public class Lab3 {
    // Configuration
    final static int PORT0 = 0;
    final static int PORT1 = 1;
    final static int MAXLOAD = 10;

    public static void main(String args[]) {
        final int NUM_CARS = MAXLOAD;
        int i;

        Ferry fer = new Ferry(PORT0, 10);

        Auto[] automobile = new Auto[NUM_CARS];
        for (i = 0; i < 7; i++) automobile[i] = new Auto(i, PORT0, fer);
        for (; i < NUM_CARS; i++) automobile[i] = new Auto(i, PORT1, fer);

        Ambulance ambulance = new Ambulance(PORT0, fer);

			/* Start the threads */
        fer.start();   // Start the ferry thread.
        for (i = 0; i < NUM_CARS; i++) automobile[i].start();  // Start automobile threads
        ambulance.start();  // Start the ambulance thread.

        try {
            fer.join();
        } catch (InterruptedException e) {
            //ignored
        }

        // Wait until ferry terminates.
        System.out.println("Ferry stopped.");


        // Stop other threads.
        for (i = 0; i < NUM_CARS; i++) automobile[i].interrupt(); // Let's stop the auto threads.

        ambulance.interrupt(); // Stop the ambulance thread.
    }
}


class Auto extends Thread { // Class for the auto threads.

    private int id_auto;
    private int port;
    private Ferry fry;

    public Auto(int id, int prt, Ferry ferry) {
        this.id_auto = id;
        this.port = prt;
        this.fry = ferry;
    }

    public void run() {

        while (true) {
            // Delay
            try {
                sleep((int) (300 * Math.random()));
            } catch (Exception e) {
                break;
            }
            System.out.println("Auto " + id_auto + " arrives at port " + port);

            Semaphore hold; //hold until the cars can board

            //determine the port the and set the semaphore to that port
            if (port == Lab3.PORT0) {
                hold = fry.getPort0();
            } else {
                hold = fry.getPort1();
            }

            try {
                //acquire a hold on that semaphore to ensure our position
                hold.acquire();
            } catch (InterruptedException e) {
                break;
            }

            // Board
            System.out.println("Auto " + id_auto + " boards on the ferry at port " + port);
            fry.addLoad();  // increment the ferry load

            //check if the ferry is full and if so head out
            if (fry.getLoad() == Lab3.MAXLOAD) {
                fry.getTravel().release();
            } else {
                //otherwise release the hold allowing the next car/ambo to load on
                hold.release();
            }

            // Arrive at the next port
            port = 1 - port;

            //we've switched ports so focus the new semaphore
            if (port == Lab3.PORT0) {
                hold = fry.getPort0();
            } else {
                hold = fry.getPort1();
            }

            // disembark
            try {
                fry.getDisembark().acquire();
            } catch (InterruptedException e) {
                break;
            }

            System.out.println("Auto " + id_auto + " disembarks from ferry at port " + port);
            fry.reduceLoad();   // Reduce load

            //if the ferry is empty allow cars to board
            if (fry.getLoad() == 0) {
                hold.release(); // signal to cars to board
            } else {
                fry.getDisembark().release(); // signal next auto to disembark
            }

            // Terminate
            if (isInterrupted()) {
                break;
            }
        }
        System.out.println("Auto " + id_auto + " terminated");
    }

}

class Ambulance extends Thread { // the Class for the Ambulance thread

    private int port;
    private Ferry fry;

    public Ambulance(int prt, Ferry ferry) {
        this.port = prt;
        this.fry = ferry;
    }

    public void run() {
        while (true) {
            // Attente
            try {
                sleep((int) (1000 * Math.random()));
            } catch (Exception e) {
                break;
            }
            System.out.println("Ambulance arrives at port " + port);

            Semaphore hold;

            //set the hold to the port we are at
            if (port == Lab3.PORT0) {
                hold = fry.getPort0();
            } else {
                hold = fry.getPort1();
            }

            //Board
            try {
                //aquire a hold on that port
                hold.acquire();
            } catch (InterruptedException e) {
                break;
            }

            System.out.println("Ambulance boards the ferry at port " + port);
            fry.addLoad();
            fry.getTravel().release(); //Advise the ferry to leave

            // Arrive at the next port
            port = 1 - port;

            //determine the new port
            if(port == Lab3.PORT0) {
                hold = fry.getPort0();
            } else {
                hold = fry.getPort1();
            }

            //Disembark
            try {
                //hold on the disembarkment -- until we arrive at the new port
                fry.getDisembark().acquire();
            } catch (InterruptedException e) {
                break;
            }

            //Disembarkment
            System.out.println("Ambulance disembarks the ferry at port " + port);
            fry.reduceLoad();   // Reduce load

            if(fry.getLoad() == 0) {
                //allow cars to board
                hold.release();
            } else {
                //allow the next car to disembark
                fry.getDisembark().release();
            }

            // Terminate
            if (isInterrupted()) {
                break;
            }
        }
        System.out.println("Ambulance terminates.");
    }

}

class Ferry extends Thread { // The ferry Class

    private int port = 0;  // Start at port 0
    private int load = 0;  // Load is zero
    private int numCrossings;  // number of crossings to execute
    //Semaphores
    private Semaphore port0;
    private Semaphore port1;
    private Semaphore disembark;
    private Semaphore travel;

    public Ferry(int prt, int nbtours) {
        this.port = prt;
        numCrossings = nbtours;

        port0 = new Semaphore(0, true);
        port1 = new Semaphore(0, true);
        disembark = new Semaphore(0, true);
        travel = new Semaphore(0, true);
    }

    public void run() {
        int i;
        System.out.println("Start at port " + port + " with a load of " + load + " vehicles");

        //determine which port we are at and allow cars to board
        if (port == Lab3.PORT0) {
            port0.release();
        } else {
            port1.release();
        }

        // numCrossings crossings in our day
        for (i = 0; i < numCrossings; i++) {
            //hold while we travel
            try {
                travel.acquire();
            } catch (InterruptedException e) {
                //ignored
            }

            // The crossing
            System.out.println("Departure from port " + port + " with a load of " + load + " vehicles");
            System.out.println("Crossing " + i + " with a load of " + load + " vehicles");
            port = 1 - port;

            try {
                sleep((int) (100 * Math.random()));
            } catch (Exception e) {
                break;
            }

            // Arrive at port
            System.out.println("Arrive at port " + port + " with a load of " + load + " vehicles");
            // Disembarkment et loading
            disembark.release();
        }
    }

    // methodes to manipulate the load of the ferry
    public int getLoad() {
        return load;
    }

    public void addLoad() {
        load = load + 1;
    }

    //Some getters to access the semaphores used to control the ferry

    public void reduceLoad() {
        load = load - 1;
    }

    public Semaphore getPort0() {
        return port0;
    }

    public Semaphore getPort1() {
        return port1;
    }

    public Semaphore getDisembark() {
        return disembark;
    }

    public Semaphore getTravel() {
        return travel;
    }
}
