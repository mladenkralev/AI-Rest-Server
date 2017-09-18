package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utll.Constants;

import java.io.File;
import java.io.IOException;

/**
 * Created by mladen on 9/2/2017.
 */
@RestController
public class ShowContentInFolder {
    private String pathToFile = Constants.rootPath + File.separator + "src" + File.separator + "main" +
            File.separator + "resources" + File.separator + "mnist_png" +
            File.separator + "testing" + File.separator;

    @RequestMapping(value= "/show", method = RequestMethod.GET)
    public String showContent(@RequestParam(value = "number", defaultValue = "0") String number) throws IOException {
        File dir = new File(pathToFile + number);
        StringBuffer buffer = new StringBuffer();

        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                buffer.append(file.getAbsoluteFile() + "<hr>");
            }
            return buffer.toString();
        }

        return "Bad input";
    }
}
