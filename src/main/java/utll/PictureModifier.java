package utll;

import machine.learing.model.UtilMachineLearingModel;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

/**
 * Used for utility operation with incoming images.
 * <p>
 * Created by mladen on 8/15/2017.
 */
public class PictureModifier {
    private static Logger log = LoggerInner.getLogger();

    /**
     * Create temp folder for pictures.
     */
    static {
        File dir = new File(Constants.tempFolder);
        if (!dir.exists()) {
            log.info(String.format("Creating folder %s", dir.getAbsolutePath()));
            dir.mkdirs();
        }
    }

    /**
     * Converts the original image to reversed black and white new image.
     * This means that every black pixel from the original image will be white in the new image
     * and every white pixel from original image is going to be black in the new image.
     * Saves the image to {@link Constants#tempFolder}.
     *
     * @param originalImage
     * @return reversed black and white verison of the original picture as BufferedImage
     * @throws IOException
     */
    public static BufferedImage convertToReversedBlackWhite(BufferedImage originalImage) throws IOException {
        if (originalImage == null) {
            throw new IllegalArgumentException("Null passed as parameter for original image");
        }

        String reversedBlackAndWhiteImageName = "outputReversedBlack.jpg";

        BufferedImage blackWhite = convertToBlackWhite(originalImage);
        BufferedImage reversedBlackWhite =
                new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        File reverseBlackWhiteFile = new File(Constants.tempFolder + File.separator + reversedBlackAndWhiteImageName);

        Color white = new Color(255, 255, 255); // Color white
        Color black = new Color(0, 0, 0); // Color black

        int rgbWhite = white.getRGB();
        int rgbBlack = black.getRGB();

        for (int indexX = 0; indexX < blackWhite.getWidth(); indexX++) {
            for (int indexY = 0; indexY < blackWhite.getWidth(); indexY++) {
                int rgba = blackWhite.getRGB(indexX, indexY);

                // Color object representing [rgba] of image index that we are proceeding
                Color col = new Color(rgba, true);
                if ((col.getRed() == 0) && (col.getBlue() == 0) && (col.getGreen() == 0)) {
                    reversedBlackWhite.setRGB(indexX, indexY, rgbWhite);
                } else {
                    reversedBlackWhite.setRGB(indexX, indexY, rgbBlack);
                }
            }
        }
        log.info("Created the new reversed black and white image successuflly");

        ImageIO.write(reversedBlackWhite, "jpg", reverseBlackWhiteFile);
        log.info(String.format("Added %s ", Constants.tempFolder + reversedBlackAndWhiteImageName));

        return reversedBlackWhite;
    }

    /**
     * Converts the original image to black and white version.
     * Saves the image to {@link Constants#tempFolder}.
     *
     * @param originalImage
     * @return black and white verison of the original picture
     * @throws IOException
     */
    public static BufferedImage convertToBlackWhite(BufferedImage originalImage) throws IOException {
        if (originalImage == null) {
            throw new IllegalArgumentException("Null passed as parameter for original image");
        }

        BufferedImage blackWhite;

        String blackAndWhiteImageName = "outputBlack.jpg";

        File blackWhiteFile = new File(Constants.tempFolder + blackAndWhiteImageName);

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(originalImage, originalImage);

        blackWhite = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D blackAndWhiteGraphics = blackWhite.createGraphics();
        blackAndWhiteGraphics.drawImage(originalImage, 0, 0, null);
        blackAndWhiteGraphics.dispose();
        log.info("Created the new black and white graphics object successuflly");

        ImageIO.write(blackWhite, "jpg", blackWhiteFile);
        log.info(String.format("Added %s ", Constants.tempFolder + blackAndWhiteImageName));

        return blackWhite;
    }

    /**
     * Converts the original image to gray scale version.
     * Used for testing the real life picture.
     * <p>
     * Saves the image to {@link Constants#tempFolder}.
     *
     * @param originalImage
     * @return black and white verison of the original picture
     * @throws IOException
     */
    public static BufferedImage convertToGrayScale(BufferedImage originalImage) throws IOException {
        if (originalImage == null) {
            throw new IllegalArgumentException("Null passed as parameter for original image");
        }

        String grayImageName = "outputGray.jpg";

        BufferedImage grayScale = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        File grayFile = new File(Constants.tempFolder + grayImageName);

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(grayScale, grayScale);

        Graphics2D grayScaleGraphics = grayScale.createGraphics();
        grayScaleGraphics.drawImage(originalImage, 0, 0, null);
        grayScaleGraphics.dispose();
        log.info("Created the  new gray scale graphics object successuflly");

        ImageIO.write(grayScale, "jpg", grayFile);
        log.info(String.format("Added %s ", Constants.tempFolder + grayImageName));

        return grayScale;
    }

    /**
     * Resize the original image to size:
     * <p>
     * Width: {@link UtilMachineLearingModel#widthImage}
     * Height: {@link UtilMachineLearingModel#heightImage}
     *
     * @param originalImage
     * @return black and white verison of the original picture
     * @throws IOException
     */
    public static BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
        if (originalImage == null) {
            throw new IllegalArgumentException("Null passed as parameter for original image");
        }

        String resizedImageName = "outputResized.jpg";

        BufferedImage newResizedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);

        File resizedFile = new File(Constants.tempFolder + resizedImageName);

        Graphics resizedReversedBlackWhite = newResizedImage.createGraphics();
        resizedReversedBlackWhite.drawImage(originalImage, 0, 0, UtilMachineLearingModel.widthImage,
                UtilMachineLearingModel.heightImage, null);
        resizedReversedBlackWhite.dispose();
        log.info("Created the new resized graphics object successuflly");

        ImageIO.write(newResizedImage, "jpg", resizedFile);
        log.info(String.format("Added %s ", Constants.tempFolder + resizedImageName));

        return newResizedImage;
    }
}
