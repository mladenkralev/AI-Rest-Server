package junit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import utll.LoggerInner;
import utll.PictureModifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mladen on 8/19/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {application.Application.class})
@WebAppConfiguration
public class PictureModifierTest {
    private static String rootPath = System.getProperty("user.dir");
    private static String tempFolder = rootPath + File.separator + "tmpFiles" + File.separator;
    private static Logger log = LoggerInner.getLogger();

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private static File imageFile;

    @Before
    public void init() throws IOException {
        ClassLoader classLoader = PictureModifierTest.class.getClassLoader();
        imageFile = new File(classLoader.getResource("maxresdefault.jpg").getFile());
        if (Files.exists(Paths.get(tempFolder))) {
            log.info("Deleting temp folder. Files that are deleted: ");
            Files.walk(Paths.get(tempFolder), FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        }
    }

    @Test
    public void checkForNullPointer() throws IOException {
        exception.expect(IllegalArgumentException.class);

        PictureModifier.convertToBlackWhite(null);
        PictureModifier.convertToGrayScale(null);
        PictureModifier.convertToReversedBlackWhite(null);
        PictureModifier.resizeImage(null);
    }

    @Test
    public void createFiles() throws IOException {
        assertFalse(Files.exists(Paths.get(tempFolder)));

        BufferedImage image = ImageIO.read(imageFile);
        log.info("Original image is " + Paths.get(String.valueOf(imageFile)));

        assertTrue(PictureModifier.convertToBlackWhite(image) != null);
        assertTrue(PictureModifier.convertToGrayScale(image) != null);
        assertTrue(PictureModifier.convertToReversedBlackWhite(image) != null);
        assertTrue(PictureModifier.resizeImage(image) != null);

        String reversedBlackAndWhiteImageName = "outputReversedBlack.jpg";
        String blackAndWhiteImageName = "outputBlack.jpg";
        String resizedImageName = "outputResized.jpg";
        String grayImageName = "outputGray.jpg";

        assertTrue(Files.exists(Paths.get(tempFolder + reversedBlackAndWhiteImageName)));
        assertTrue(Files.exists(Paths.get(tempFolder + grayImageName)));
        assertTrue(Files.exists(Paths.get(tempFolder + blackAndWhiteImageName)));
        assertTrue(Files.exists(Paths.get(tempFolder + resizedImageName)));
    }
}
