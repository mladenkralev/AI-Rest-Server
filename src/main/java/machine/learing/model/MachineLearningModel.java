package org.deeplearning4j.examples;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utll.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Used to load model.
 * <p>
 * Created by mladen on 7/30/2017.
 */
public class MachineLearningModel {
    private static Logger log = LoggerFactory.getLogger(MachineLearningModel.class);

    private static int height = 28;
    private static int width = 28;
    private static int channels = 1;

    /**
     * Guess the digit for saved picture on local storage.
     *
     * @param pathToImage path to local storage image that needs to be processed
     * @return digit with the highest probability that is calculated from the model
     * @throws IOException
     */
    public static int guessDigit(Path pathToImage) throws IOException {

        // recordReader.getLabels()
        List<Integer> labelList = Arrays.asList(2, 3, 7, 1, 6, 4, 0, 5, 8, 9);

        MultiLayerNetwork model = loadModel(Paths.get(Constants.rootPath + File.separator + "trained_mnist_model.zip"));

        log.info("*********TEST YOUR IMAGE AGAINST SAVED NETWORK********");
        File file = pathToImage.toFile();

        // Use NativeImageLoader to convert to numerical matrix
        NativeImageLoader loader = new NativeImageLoader(height, width, channels);

        // Get the image into an INDarray
        INDArray image = loader.asMatrix(file);

        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);

        // Pass through to neural Net
        INDArray output = model.output(image);
        log.info("## The FILE CHOSEN WAS " + pathToImage);
        log.info("## The Neural Nets Pediction ##");
        log.info("## list of probabilities per label ##");
        log.info("## List of Labels in Order## ");
        log.info(output.toString());
        log.info(labelList.toString());

        int result = findBiggestPropability(output);
        return result;
    }

    /**
     * Iterate through the array and find the biggest probability.
     * The biggest value is  most likely the digit on the image that is passed in {@link #guessDigit(Path)} method}.
     *
     * @param output
     * @return biggest propabily number
     */
    private static int findBiggestPropability(INDArray output) {
        float max = 0;
        int maxIndex = -1;
        for (int index = 0; index < output.length(); index++) {
            log.info("Index [" + index + "]: " + output.getColumn(index) + ".");
            if (max < output.getFloat(index)) {
                max = output.getFloat(index);
                maxIndex = index;
            }
        }

        System.out.println("Your number is: " + maxIndex + " with probability:[" + max + "]");
        return maxIndex;
    }

    /**
     * Loads the model form location
     *
     * @param pathToModel path to model that is going to be loaded
     * @return model
     * @throws IOException
     */
    private static MultiLayerNetwork loadModel(Path pathToModel) throws IOException {
        File modelFile = pathToModel.toFile();

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(modelFile);
        return model;

    }
}