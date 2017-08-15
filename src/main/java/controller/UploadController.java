package controller;

import org.deeplearning4j.examples.MachineLearningModel;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import picture.PictureModifier;
import utll.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mladen on 8/15/2017.
 */
@RestController
public class UploadController {
    private static Logger log = utll.Logger.getLogger();

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (saveImageToServer(file)) {


                BufferedImage original = ImageIO.read(new File(Constants.tempPicture));

                PictureModifier.convertToBlackWhite(original);

                original = ImageIO.read(new File(Constants.tempPicture));

                BufferedImage reversedBlackWhite = PictureModifier.convertToReversedBlackWhite(original);

                PictureModifier.resizeImage(reversedBlackWhite);

                int number = MachineLearningModel.guessDigit(Constants.tempFolder + File.separator + "outputResized.jpg");
                log.info(String.format("Returning digit %d", number));

                return "Your number is " + number;
            } else {
                return "Error occoured when adding a picture";
            }
        } catch (IOException e) {
            log.info("Exception is throwned: " + e.getMessage() + "\nStacktarace:" + e.getStackTrace());
            return "Error occoured when adding a picture";
        }

    }

    private boolean saveImageToServer(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();

            // Create the file on server
            File serverFile = new File(Constants.tempPicture);

            File dir = new File(Constants.tempFolder);
            if (!dir.exists()) {
                log.info(String.format("Creating folder %s", dir.getAbsolutePath()));
                dir.mkdirs();
            }

            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            stream.write(bytes);

            log.info(String.format("Creating file %s", serverFile.getAbsolutePath()));

            stream.close();
            log.info(String.format("Successuflly closing the stream"));

            return true;
        }

        log.info("File was empty!");
        return false;
    }
}


