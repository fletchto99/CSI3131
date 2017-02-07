import java.awt.*;      

/* Global definitions */

class MBGlobals {
    public Color[] mcolors =   // color palette for creating diagram
            {
                    new Color(0, 0, 20),
                    new Color(0, 0, 40),
                    new Color(0, 0, 50),
                    new Color(0, 0, 60),
                    new Color(0, 0, 70),
                    new Color(0, 0, 80),
                    new Color(0, 0, 90),
                    new Color(0, 0, 100),
                    new Color(0, 0, 110),
                    new Color(0, 0, 120),
                    new Color(0, 0, 130),
                    new Color(0, 0, 135),
                    new Color(0, 0, 140),
                    new Color(0, 0, 145),
                    new Color(0, 0, 150),
                    new Color(0, 0, 155),
                    new Color(0, 0, 160),
                    new Color(0, 0, 165),
                    new Color(0, 0, 170),
                    new Color(0, 0, 175),
                    new Color(0, 0, 190),
                    new Color(0, 0, 195),
                    new Color(0, 0, 210),
                    new Color(0, 0, 215),
                    new Color(0, 0, 220),
                    new Color(0, 0, 225),
                    new Color(0, 0, 230),
                    new Color(0, 0, 235),
                    new Color(0, 0, 240),
                    new Color(0, 0, 244),
                    new Color(0, 0, 250),
                    Color.black
            };

    // How many iteration before declaring a point 'in'
    public int maxit = mcolors.length - 1;

    // The left top corner of the picture (real values)
    public double min_r = 0;
    public double min_i = 0;

    // Steps per pixel in the real and imaginary axes
    public double step_r = 1;
    public double step_i = 1;

    // Dimension of the canvas in pixels (square)
    public int pixeldim;

    // Fill in the boxes of this size
    public int minBoxSize = 10;

    public MBGlobals(double mr, double mi,  // Real ulh corner coordinates
                     double stepsz,   // step size per pixel
                     int pxdim,   // dimension of canvas in pixels
                     int minbs)   // size of square to fill
    {
        min_r = mr;
        min_i = mi;
        step_r = stepsz;
        step_i = stepsz;
        pixeldim = pxdim;
        minBoxSize = minbs;
    }
}

