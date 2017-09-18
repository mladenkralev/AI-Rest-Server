package machine.learing.model;

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

import utll.Constants;
import static machine.learing.model.UtilMachineLearingModel.*;

import java.io.File;

/**
 * TODO: After successuflly determinating that a digit on image is same as digit that is guessed
 * TODO: 1) Save the image as training data
 * TODO: 2) Load the new model and let theserver work with it
 * TODO: 3) This should be done once a day, because creating,saving and loading a model is slow operation.
 */

/**
 * Class used for loading model.
 * Not currently used.
 *
 * Created by mladen on 7/30/2017.
 */
public class LoadModel {
    private static Logger log = LoggerFactory.getLogger(LoadModel.class);

    public static final String PATH_TRAINING_DATA = Constants.rootPath + "\\src\\main\\resources\\mnist_png\\training";
    public static final String PATH_TEST_DATA = Constants.rootPath + "\\src\\main\\resources\\mnist_png\\testing";

    public static void main(String[] args) throws Exception {
        // Define the File Paths
        File trainData = new File(PATH_TRAINING_DATA);
        File testData = new File(PATH_TEST_DATA);

        // Define the FileSplit(PATH, ALLOWED FORMATS,random)
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        // Extract the parent path as the image label
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        ImageRecordReader recordReader = new ImageRecordReader(heightImage, widthImage, channels, labelMaker);

        // Initialize the record reader
        // add a listener, to extract the name
        recordReader.initialize(train);

        // DataSet Iterator
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // Scale pixel values to 0-1
        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);

        // Build Our Neural Network
        log.info("******LOAD TRAINED MODEL******");

        // Where to save model
        File locationToSave = new File("trained_mnist_model.zip");

        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToSave);
        model.getLabels();

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
