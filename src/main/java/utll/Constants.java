package utll;

import java.io.File;

/**
 * Created by mladen on 8/15/2017.
 */
public class Constants {
    public static String rootPath = System.getProperty("user.dir");
    public static String tempFolder = rootPath + File.separator + "tmpFiles" + File.separator;
    public static String tempPicture = tempFolder + File.separator + "inputPicture.jpg";

    private Constants() {
        throw new IllegalArgumentException("Can't access private class");
    }
}
