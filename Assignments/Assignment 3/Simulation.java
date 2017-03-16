import java.io.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class Simulation extends Thread {

    private int id;
    private int numVacationers;
    private int numRods;
    private int numBait;
    private boolean containsSlacker;

    public Simulation(int id, int numVacationers, int numRods, int numBait, boolean containsSlacker) throws FileNotFoundException {
        this.id = id;
        this.numVacationers = numVacationers;
        this.numRods = numRods;
        this.numBait = numBait;
        this.containsSlacker = containsSlacker;
    }

    public void run() {
        try {
            log("Starting simulation " + id);

            // Simulate 5 days and collect the totals
            int totals[] = new int[numVacationers];
            for (int day = 1; day <= 5; day++) {
                int dayTotals[] = simulateDay(day);
                for (int i = 0; i < numVacationers; i++) {
                    totals[i] += dayTotals[i];
                }
            }

            // Show simulation results
            System.out.println("Ending simulation...");
            System.out.println("Average number of fishes caught (per person): ");
            System.out.print("\t");

            //calculate the totals
            int total = 0;
            for (int i = 0; i < numVacationers; i++) {
                total += totals[i];
                System.out.print((totals[i] / 5));
                if (i != numVacationers - 1) {
                    System.out.print(", ");
                }
            }

            System.out.println();
            System.out.println();
            System.out.println("Total fish caught: " + total);
            System.out.flush();

            log("End of simulation.");
        } catch (InterruptedException ignored) {
        }
    }

    private int[] simulateDay(int day) throws InterruptedException {
        System.out.println("Simulating day " + day);

        // Create semaphores for the resources
        Semaphore rods = new Semaphore(numRods, true);
        Semaphore bait = new Semaphore(numBait, true);

        // Randomly select a slacker if the simulation requests it
        int slacker = containsSlacker ? ThreadLocalRandom.current().nextInt(numVacationers) : -1;

        // Create and start the vacationer threads
        Vacationer[] vacationers = new Vacationer[numVacationers];
        Thread[] threads = new Thread[numVacationers];
        for (int i = 0; i < numVacationers; i++) {
            vacationers[i] = new Vacationer(i, rods, bait, i == slacker);
            threads[i] = new Thread(vacationers[i]);
            threads[i].start();
        }

        // Wait "8" hours (24 seconds)
        Thread.sleep(24000);
        System.out.println("End of day, interrupt vacationers");
        System.out.println();

        // Interrupt all vacationer threads since the day has ended
        for (Thread thread : threads) {
            thread.interrupt();
        }

        // Compute the total number of fish caught for the day
        int totals[] = new int[numVacationers];
        for (int i = 0; i < numVacationers; i++) {
            threads[i].join();
            totals[i] = vacationers[i].getNumCaught();
        }

        //the total number of fishes caught
        return totals;
    }

    private void log(String message) {
        System.out.println("[" + id + "] " + message);
    }
}
