package controller;

import machine.learing.model.MachineLearningModel;
import model.GuessedDigit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utll.Constants;
import utll.LoggerInner;
import utll.PictureModifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Handles uploading multipart/form-data as post request.
 * <p>
 * Created by mladen on 8/15/2017.
 */
@RestController
public class UploadController {
    private static org.slf4j.Logger log = LoggerInner.getLogger();

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public GuessedDigit handleFileUpload(@RequestParam("file") MultipartFile file) {
        if ((file.isEmpty()) || (file.getSize() == 0)) {
            log.info("File was empty! Ignoring request.");
            return new GuessedDigit("\"file\" needs to be set in url.", -1);
        }

        try {
            if (saveImageToServer(file)) {

                BufferedImage original = ImageIO.read(new File(Constants.tempPicture));

                PictureModifier.convertToBlackWhite(original);

                original = ImageIO.read(new File(Constants.tempPicture));

                BufferedImage reversedBlackWhite = PictureModifier.convertToReversedBlackWhite(original);

                PictureModifier.resizeImage(reversedBlackWhite);

                int number = MachineLearningModel.guessDigit(Paths.get(Constants.tempFolder + File.separator + "outputResized.jpg"));
                log.info(String.format("Returning digit %d", number));

                return new GuessedDigit("Your number is", number);
            } else {
                return new GuessedDigit("Error occoured when adding a picture", -1);
            }
        } catch (IOException e) {
            log.info("Exception is throwned: " + e.getMessage() + "\nStacktarace:" + e.getStackTrace());
            return new GuessedDigit("IO Exception was thrown", -1);
        }

    }

    /**
     * Saving incoming image in server.
     *
     * @param file to save
     * @return true if file was created and saved to local storage successfully, false otherwise
     * @throws IOException
     */
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


