import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class Vacationer implements Runnable {

    private int id;
    private Semaphore rods;
    private Semaphore bait;
    private int bucket;
    private boolean slacker;

    public Vacationer(int id, Semaphore rods, Semaphore bait, boolean slacker) {
        this.id = id;
        this.rods = rods;
        this.bait = bait;
        this.bucket = 0;
        this.slacker = slacker;
    }

    public void run() {
        while (true) {
            try {
                fish();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void fish() throws InterruptedException {
        //Hold a rod
        rods.acquire();
        log("Acquired 1 out of " +
                (rods.availablePermits() + 1) +
                " available rods");

        //Acquire some bait
        bait.acquire();
        log("Acquired 1 out of " +
                (bait.availablePermits() + 1) +
                " available bait");

        // Fish for a maximum of 20 minutes
        log("Started to fish");
        Thread.sleep(1000);

        // Catch random number of fish between 0-10
        int numCaught = ThreadLocalRandom.current().nextInt(11);
        log("Caught " + numCaught + " fish while fishing");
        this.bucket += numCaught;

        //Release the bait hold
        bait.release();
        log("Returned 1 bait");

        // Release fishing rod after 1 minute, or 2 if a slacker
        if (!slacker) {
            Thread.sleep(50);
        } else {
            Thread.sleep(100);
        }

        //Release the rods
        rods.release();
        log("Returned 1 rod");
    }

    private void log(final String message) {
        System.out.println("[" + id + "] " + message);
    }

    public int getNumCaught() {
        return this.bucket;
    }
}
