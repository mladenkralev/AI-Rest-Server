package utll;

import org.apache.log4j.Logger;
/**
 * Created by mladen on 8/15/2017.
 */

public class Logger {
    private static Logger loger = org.apache.log4j.Logger.getLogger(Logger.class);

    static {

    }

    private Logger() {
        throw new IllegalArgumentException("Can't access private class");
    }

    public static org.slf4j.Logger getLogger() {
        return loger;
    }
}
