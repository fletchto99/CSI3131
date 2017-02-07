import java.awt.*;


// Note that this class can be run within a thread
public class MBPaint implements Runnable {
    private MBCanvas mbcv;     // For saving reference to the canvas object
    private MBGlobals mglob;   // For saving reference to the globals
    private Rectangle mrect;   // Reference to square for which this object is responsible

    public MBPaint(MBCanvas cv, MBGlobals mg, Rectangle rect) {
        mbcv = cv;    // Save reference to the canvas object
        mglob = mg;    // Save reference to the globals
        mrect = rect;  // Reference to square for which this object is responsible
    }

    public void run()   // This method does all the work.
    {
        int i, j;

        // Compute the maximum pixel values for hor (i) and vert (j)
        int maxi = mrect.x + mrect.width;
        int maxj = mrect.y + mrect.height;

        // Can now compute for rectangle
        for (i = mrect.x; i < maxi; i++)
            for (j = mrect.y; j < maxj; j++) setPoint(i, j);
    }

    /* Sets the point on the canvas according to Mandelbrot function - see mandPixel */
    private void setPoint(int i, int j) {
        int col;

        col = mandPixel(mglob.min_r + i * mglob.step_r, mglob.min_i - j * mglob.step_i);
        Graphics cvgr = mbcv.getGraphics();  // need Graphics object to write to canvas
        cvgr.setColor(mglob.mcolors[col]);
        cvgr.drawLine(i, j, i, j);           // tells the server to draw it
    }

    /* computes the mandlebrot function for one pixel*/
    /* The function basically computes the following
     * Z = Z*Z + C, where Z and C are imaginary numbers
     * Note that C corresponds to the point on the plane
     * being displayed.
     * Z is computed iteratively to see if the length 
     * from the origine exceeds 2 within a certain
     * number of iterations.
     * If Z eventually exceeds 2, then the iteration
     * number at which it exceeded 2, will be used 
     * to select the color (an index into a color palette)
     * If Z remains below 2 for the maximum number of
     * iterations, then the color selected for the point
     * will be black */
    private int mandPixel(double c_r, double c_i) {
        int iterate;
        double temp_r, lengthsq;
        double z_r = 0.0, z_i = 0.0;

        iterate = 0;
        do {
            temp_r = z_r * z_r - z_i * z_i + c_r;
            z_i = 2.0 * z_r * z_i + c_i;
            z_r = temp_r;
            lengthsq = z_r * z_r + z_i * z_i;
            iterate++;
        } while (lengthsq < 4.0 && iterate < mglob.maxit);

        return (iterate);
    }
}
