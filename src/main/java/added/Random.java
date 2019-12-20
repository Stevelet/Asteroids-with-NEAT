package added;

public class Random {
    //TODO implement methods

    public static long seed;

    public static double random(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }

    public static double random(double upperBound) {
        return 0;
    }

    public static double randomGaussian() {
        return 0;
    }

    public static void randomSeed(long seedUsed) {
        seed = seedUsed;
    }
}
