package org.deeplearning4j.examples;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Random;

/**
 * Created by mladen on 7/30/2017.
 */
public class LoadModel {
    private static Logger log = LoggerFactory.getLogger(LoadModel.class);

    public static void main(String[] args) throws Exception {
        // image information
        // 28 * 28 grayscale
        // grayscale implies single channel
        int height = 28;
        int width = 28;
        int channels = 1;
        int rngseed = 123;
        Random randNumGen = new Random(rngseed);
        int batchSize = 128;
        int outputNum = 10;
        int numEpochs = 15;

        // Define the File Paths
        File trainData = new File("D:\\deeplearning4jScreencast\\screencasts\\MLPLinearClassifier\\src\\main\\resources\\mnist_png\\training");
        File testData = new File("D:\\deeplearning4jScreencast\\screencasts\\MLPLinearClassifier\\src\\main\\resources\\mnist_png\\testing");

        // Define the FileSplit(PATH, ALLOWED FORMATS,random)

        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        // Extract the parent path as the image label

        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);

        // Initialize the record reader
        // add a listener, to extract the name

        recordReader.initialize(train);
        //recordReader.setListeners(new LogRecordListener());

        // DataSet Iterator

        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // Scale pixel values to 0-1

        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);


        // Build Our Neural Network


        log.info("******LOAD TRAINED MODEL******");
        // Details

        // Where to save model
        File locationToSave = new File("trained_mnist_model.zip");

        // boolean save Updater
        //boolean saveUpdater = false;

        // ModelSerializer needs modelname, saveUpdater, Location

        //ModelSerializer.writeModel(model,locationToSave,saveUpdater);


        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);


        model.getLabels();


        //recordReader.reset();

        recordReader.initialize(test);
        DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);
        scaler.fit(testIter);
        testIter.setPreProcessor(scaler);

        // Create Eval object with 10 possible classes
        Evaluation eval = new Evaluation(outputNum);


        while (testIter.hasNext()) {
            DataSet next = testIter.next();
            INDArray output = model.output(next.getFeatureMatrix());
            eval.eval(next.getLabels(), output);

        }

        log.info(eval.stats());


    }
}
