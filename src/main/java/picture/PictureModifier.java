package picture;

import org.slf4j.Logger;
import utll.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

/**
 * Created by mladen on 8/15/2017.
 */
public class PictureModifier {
    private static Logger log = utll.Logger.getLogger();




    static {
        File dir = new File(Constants.tempFolder);
        if (!dir.exists()) {
            log.info(String.format("Creating folder %s", dir.getAbsolutePath()));
            dir.mkdirs();
        }
    }

    public static BufferedImage convertToReversedBlackWhite(BufferedImage originalImage) throws IOException {
        BufferedImage blackWhite = convertToBlackWhite(originalImage);
        BufferedImage reversedBlackWhite =
                new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        File reverseBlackWhiteFile = new File(Constants.tempFolder + File.separator + "outputReversedBlack.jpg");

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

        ImageIO.write(reversedBlackWhite, "jpg", reverseBlackWhiteFile);
        return reversedBlackWhite;
    }

    public static BufferedImage convertToBlackWhite(BufferedImage originalImage) throws IOException {
        BufferedImage blackWhite;

        File blackWhiteFile = new File(Constants.tempFolder + "outputBlack.jpg");

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(originalImage, originalImage);

        blackWhite = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D blackAndWhiteGraphics = blackWhite.createGraphics();
        blackAndWhiteGraphics.drawImage(originalImage, 0, 0, null);
        blackAndWhiteGraphics.dispose();

        ImageIO.write(blackWhite, "jpg", blackWhiteFile);
        return blackWhite;
    }

    public static BufferedImage convertToGrayScale(BufferedImage originalImage) throws IOException {
        BufferedImage grayScale = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        File grayFile = new File(Constants.tempFolder + "outputGray.jpg");

        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(grayScale, grayScale);

        Graphics2D grayScaleGraphics = grayScale.createGraphics();
        grayScaleGraphics.drawImage(originalImage, 0, 0, null);
        grayScaleGraphics.dispose();
        ImageIO.write(grayScale, "jpg", grayFile);
        return grayScale;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage) throws IOException {
        BufferedImage newResizedImage = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);

        File resizedFile = new File(Constants.tempFolder + "outputResized.jpg");

        Graphics resizedReversedBlackWhite = newResizedImage.createGraphics();
        resizedReversedBlackWhite.drawImage(originalImage, 0, 0, 28, 28, null);
        resizedReversedBlackWhite.dispose();

        ImageIO.write(newResizedImage, "jpg", resizedFile);
        return newResizedImage;
    }
}
