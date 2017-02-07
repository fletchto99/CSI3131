import java.io.IOException;

public class MandelBrot {
    public static void main(String[] args) throws IOException {

        if (args.length < 5) {
            System.out.println("Usage: java MandelBrot <CornerX> <CornerY> <Size> <SizePixels> <MinBox>");
            System.exit(1);
        }

        try {
            System.out.println("Hello - Creating MandelBrot Diagram");
            new MBFrame(Double.valueOf(args[0]),
                    Double.valueOf(args[1]),
                    Double.valueOf(args[2]),
                    Integer.valueOf(args[3]),
                    Integer.valueOf(args[4]));
        } catch (NumberFormatException e) {
            System.out.println("Error in arguments" + e);
            System.out.println("Usage: java MandelBrot <CornerX> <CornerY> <Size> <SizePixels> <MinBox>");
        }

    }
}

