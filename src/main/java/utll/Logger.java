package utll;

import org.slf4j.LoggerFactory;

/**
 * Created by mladen on 8/15/2017.
 */
public class Logger {
    private static org.slf4j.Logger loger = LoggerFactory.getLogger(Logger.class);

    private Logger() {
        throw new IllegalArgumentException("Can't access private class");
    }

    public static org.slf4j.Logger getLogger() {
        return loger;
    }
}
