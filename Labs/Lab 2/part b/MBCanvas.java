import sun.swing.SwingUtilities2;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*-----------------------------------------------------------
 *
 * This is the component onto which the diagram is displayed
 *
 * ----------------------------------------------------------*/

public class MBCanvas extends Canvas {
    private MBGlobals mg;   // reference to global definitions
    private ExecutorService threadPool;

    public MBCanvas(MBGlobals mGlob) {
        mg = mGlob;
        setSize(mg.pixeldim, mg.pixeldim);
        threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void paint(Graphics g)  // this method paints the canvas
    {
       /* reset screen to blank */
        g.setColor(Color.white);
        g.fillRect(0, 0, mg.pixeldim, mg.pixeldim);

	  /* Call method to add MandelBrot pattern */
	  /* Run MBCompute in this thread */
        Rectangle nrect = new Rectangle(0, 0, mg.pixeldim, mg.pixeldim);

        findRectangles(nrect, threadPool);
    }

    private void findRectangles(Rectangle mrect, ExecutorService pool) {
        Rectangle nrect;

        // Compute the maximum pixel values for hor (i) and vert (j)
        int maxi = mrect.x + mrect.width;

        // Only when the square is small enough do we fill
        if ((maxi - mrect.x) <= mg.minBoxSize) {
            // Can now do the painting
            // Threads have more overhead compared to a thread pool
            //Thread t = new Thread(mbp);
            //t.start();
            pool.execute(new MBPaint(this, mg, mrect));
            return;
        }

        // recursiverly compute the four subquadrants
        int midw = mrect.width / 2;
        int wover = mrect.width % 2;  // for widths not divisable by 2
        int midh = mrect.height / 2;
        int hover = mrect.height % 2;  // for heights not divisable by 2

        // First quadrant
        nrect = new Rectangle(mrect.x, mrect.y, midw, midh);
        findRectangles(nrect, pool); // Note executing recursive call

        // Second quadrant
        nrect = new Rectangle(mrect.x + midw, mrect.y, midw + wover, midh);
        findRectangles(nrect, pool); // Note executing recursive call

        // Third quadrant
        nrect = new Rectangle(mrect.x, mrect.y + midh, midw, midh + hover);
        findRectangles(nrect, pool); // Note executing recursive call

        // Fourth quadrant
        nrect = new Rectangle(mrect.x + midw, mrect.y + midh, midw + wover, midh + hover);
        findRectangles(nrect, pool); // Note executing recursive call
    }

}
