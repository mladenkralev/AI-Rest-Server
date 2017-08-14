package model;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mladen on 7/30/2017.
 */
public class TestModel {
    private static Logger log = LoggerFactory.getLogger(TestModel.class);

    public static String fileChose() {
        JFileChooser fc = new JFileChooser();
        int ret = fc.showOpenDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            return filename;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        int height = 28;
        int width = 28;
        int channels = 1;

        // recordReader.getLabels()
        List<Integer> labelList = Arrays.asList(2, 3, 7, 1, 6, 4, 0, 5, 8, 9);

        // pop up file chooser
        String filechose = fileChose().toString();

        //LOAD NEURAL NETWORK

        // Where to save model
        File locationToSave = new File("trained_mnist_model.zip");

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);

        log.info("*********TEST YOUR IMAGE AGAINST SAVED NETWORK********");

        // FileChose is a string we will need a file

        File file = new File(filechose);

        // Use NativeImageLoader to convert to numerical matrix

        NativeImageLoader loader = new NativeImageLoader(height, width, channels);

        // Get the image into an INDarray

        INDArray image = loader.asMatrix(file);

        // 0-255
        // 0-1
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);
        // Pass through to neural Net

        INDArray output = model.output(image);
        log.info("## The FILE CHOSEN WAS " + filechose);
        log.info("## The Neural Nets Pediction ##");
        log.info("## list of probabilities per label ##");
        log.info("## List of Labels in Order## ");
        log.info(output.toString());
        log.info(labelList.toString());

        float max = 0;
        int maxIndex = -1;
        for (int index = 0; index < output.length(); index++) {
            log.info("Index [" + index + "]: " + output.getColumn(index) + "");
            if (max < output.getFloat(index)) {
                max = output.getFloat(index);
                maxIndex = index;
            }
        }
        System.out.println("Your number is: " + maxIndex + " with probability:[" + max + "]");
    }

}
