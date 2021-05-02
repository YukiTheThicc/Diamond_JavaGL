package diamond2DGL.utils;

public class Time {

    public static long timeStartedMillis = System.currentTimeMillis();
    public static long timeStartedNano = System.nanoTime();

    public static float getTimeMillis() {
        return ((System.currentTimeMillis() - timeStartedMillis) / 1000f);
    }

    public static float getTimeNano() {
        return (float)((System.nanoTime() - Time.timeStartedNano) * 1E-9);
    }
}
