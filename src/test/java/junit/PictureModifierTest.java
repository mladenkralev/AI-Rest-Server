package test.cucumber;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mladen on 8/19/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {application.Application.class})
@WebAppConfiguration
public class PictureModifierTest {
//    private static String rootPath = System.getProperty("user.dir");
//    private static String tempFolder = rootPath + File.separator + "tmpFiles" + File.separator;
//    private static Logger log = LoggerInner.getLogger();
//
//    @Rule
//    public final ExpectedException exception = ExpectedException.none();
//    private static File imageFile;
//
//    @Before
//    public void init() throws IOException {
//        ClassLoader classLoader = PictureModifierTest.class.getClassLoader();
//        imageFile = new File(classLoader.getResource("maxresdefault.jpg").getFile());
//        if (Files.exists(Paths.get(tempFolder))) {
//            log.info("Deleting temp folder. Files that are deleted: ");
//            Files.walk(Paths.get(tempFolder), FileVisitOption.FOLLOW_LINKS)
//                    .sorted(Comparator.reverseOrder())
//                    .map(Path::toFile)
//                    .peek(System.out::println)
//                    .forEach(File::delete);
//        }
//    }
//
//    @When("^The programmer calls PictureModifier methods with null parameter$")
//    public void callingPictureModifierWithNPE() throws Throwable {
//        //
//    }
//
//    @Then("an exception shoul be thrown")
//    public void checkForNullPointer() throws IOException {
//        exception.expect(IllegalArgumentException.class);
//
//        PictureModifier.convertToBlackWhite(null);
//        PictureModifier.convertToGrayScale(null);
//        PictureModifier.convertToReversedBlackWhite(null);
//        PictureModifier.resizeImage(null);
//    }
//
//    @When("^The programmer calls PictureModifier methods with original image$")
//    public void userCallsPictureModifierMethodsWithParameter() throws Throwable {
//        //
//    }
//
//    @Then("an buffered image that is not null should be returned")
//    @And("there should be four images in <project_root>/tmpFiles")
//    public void createFiles() throws IOException {
//        assertFalse(Files.exists(Paths.get(tempFolder)));
//
//        BufferedImage image = ImageIO.read(imageFile);
//        log.info("Original image is " + Paths.get(String.valueOf(imageFile)));
//
//        assertTrue(PictureModifier.convertToBlackWhite(image) != null);
//        assertTrue(PictureModifier.convertToGrayScale(image) != null);
//        assertTrue(PictureModifier.convertToReversedBlackWhite(image) != null);
//        assertTrue(PictureModifier.resizeImage(image) != null);
//
//        String reversedBlackAndWhiteImageName = "outputReversedBlack.jpg";
//        String blackAndWhiteImageName = "outputBlack.jpg";
//        String resizedImageName = "outputResized.jpg";
//        String grayImageName = "outputGray.jpg";
//
//        assertTrue(Files.exists(Paths.get(tempFolder + reversedBlackAndWhiteImageName)));
//        assertTrue(Files.exists(Paths.get(tempFolder + grayImageName)));
//        assertTrue(Files.exists(Paths.get(tempFolder + blackAndWhiteImageName)));
//        assertTrue(Files.exists(Paths.get(tempFolder + resizedImageName)));
//    }
    private static String expectedResponse = "Your number is 8";
    MvcResult  result;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @When("get post is excuted")
    public void greetingsGet() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
         result = this.mockMvc.perform(get("/greeting")
                .accept(MediaType.APPLICATION_JSON)).andReturn();


    }
    @Then(" Then 200 is response")
    public void response() throws Exception {
        assertTrue( result.getResponse().getContentAsString() != null);
    }

}
