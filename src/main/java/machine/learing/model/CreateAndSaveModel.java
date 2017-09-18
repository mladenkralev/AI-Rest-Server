package org.deeplearning4j.examples;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.listener.impl.LogRecordListener;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.deeplearning4j.examples.UtilMachineLearingModel.*;

/**
 * Created by mladen on 7/9/2017.
 * Class that creates and saves a model using mnist model full with digit pictures
 */
public class CreateAndSaveModel {
    private static Logger log = LoggerFactory.getLogger(CreateAndSaveModel.class);

    public static void main(String[] args) throws IOException, URISyntaxException {
        // image information
        // 28 * 28 grayscale


        URL trainDataUrl = CreateAndSaveModel.class.getClassLoader().getResource("mnist_png/training");
        URL testDataUrl = CreateAndSaveModel.class.getClassLoader().getResource("mnist_png/testing");


        // Define the File Paths
        File trainData = new File(trainDataUrl.toURI());
        File testData = new File(testDataUrl.toURI());

        // Define the FileSplit(PATH, ALLOWED FORMATS,random)

        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);

        // Extract the parent path as the image label
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        ImageRecordReader recordReader = new ImageRecordReader(heightImage, widthImage, channels, labelMaker);

        // Initialize the record reader
        // add a listener, to extract the name

        recordReader.initialize(train);
        recordReader.setListeners(new LogRecordListener());

        // DataSet Iterator
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, outputNum);

        // Scale pixel values to 0-1

        DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);


        for (int i = 1; i < 3; i++) {
            DataSet ds = dataIter.next();
            System.out.println(ds);
            System.out.println(dataIter.getLabels());
        }
        log.info("**** Build Model ****");

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(rngseed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .learningRate(0.006)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(heightImage * widthImage)
                        .nOut(100)
                        .activation("relu")
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(100)
                        .nOut(outputNum)
                        .activation("softmax")
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .pretrain(false).backprop(true)
                .setInputType(InputType.convolutional(heightImage, widthImage,channels))
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        model.setListeners(new ScoreIterationListener(10));

        log.info("*****TRAIN MODEL********");
        for(int i = 0; i<numEpochs; i++){
            model.fit(dataIter);
        }

        log.info("******SAVE TRAINED MODEL******");
        // Details

        // Where to save model
        File locationToSave = new File("trained_mnist_model.zip");

        // boolean save Updater
        boolean saveUpdater = false;

        // ModelSerializer needs modelname, saveUpdater, Location

        ModelSerializer.writeModel(model,locationToSave,saveUpdater);
//
//        log.info("******EVALUATE MODEL******");
//
//        recordReader.reset();
//
//        recordReader.initialize(test);
//        DataSetIterator testIter = new RecordReaderDataSetIterator(recordReader,batchSize,1,outputNum);
//        scaler.fit(testIter);
//        testIter.setPreProcessor(scaler);
//
//        // Create Eval object with 10 possible classes
//        Evaluation eval = new Evaluation(outputNum);
//
//        while(testIter.hasNext()){
//            DataSet next = testIter.next();
//            INDArray output = model.output(next.getFeatureMatrix());
//            eval.eval(next.getLabels(),output);
//
//        }
//
//        log.info(eval.stats());



    }
}
