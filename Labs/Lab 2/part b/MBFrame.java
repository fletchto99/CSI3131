import javax.swing.*;

/*-------------------------------------------------
 * Set up a simple frame for displaying
 * MBCanvas object
 * ------------------------------------------------*/
public class MBFrame extends JFrame {
    private MBCanvas cv;

    public MBFrame(double ucx, double ucy,  // Upper left hand corner (real/imag)
                   double bxdim, // Size of the box (real values)
                   int pixeldim, // Size of box in terms of dimensions of pixels
                   int minBxSize)  // minimum size of square to start filling
    {
        setTitle("Mandlebrot Diagram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MBGlobals mg = new MBGlobals(ucx, ucy, bxdim / pixeldim, pixeldim, minBxSize);
        add(new MBCanvas(mg));
        pack();
        setResizable(false);
        setVisible(true);
    }

}
