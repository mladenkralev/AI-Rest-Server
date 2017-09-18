package utll;

import machine.learing.model.MachineLearningModel;
import org.slf4j.LoggerFactory;

/**
 * Global logger for the whole project.
 *
 * Created by mladen on 8/15/2017.
 */

public class LoggerInner {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(MachineLearningModel.class);

    private LoggerInner() {
        throw new IllegalArgumentException("Can't access private class");
    }

    public static org.slf4j.Logger getLogger() {
        return log;
    }
}
