package machine.learing.model;

import java.util.Random;

/**
 * Created by mladen on 8/19/2017.
 */
public class UtilMachineLearingModel {
    // grayscale implies single channel
    public static int heightImage = 28;
    public static int widthImage = 28;

    public static int channels = 1;
    public static int rngseed = 123;

    public static Random randNumGen = new Random(rngseed);

    public static int batchSize = 128;
    public static int outputNum = 10;
    public static int numEpochs = 3;

    private UtilMachineLearingModel() {
        throw new IllegalArgumentException("Can't access private class");
    }
}
