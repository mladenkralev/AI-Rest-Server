package junit;

/**
 * Created by mladen on 8/18/2017.
 */


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {application.Application.class})
@WebAppConfiguration
public class ServerTest {
    private static String expectedResponse = "Your number is 8";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void greetingsGet() throws Exception {
        this.mockMvc.perform(get("/greeting")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void postUploadPicture() throws Exception {
        URL originalImage = ServerTest.class
                .getClassLoader().getResource("maxresdefault.jpg");
        File file = new File(originalImage.toURI());

        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getAbsolutePath(),
                "multipart/form-data", Files.readAllBytes(file.toPath()));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/uploadFile")
                .file(multipartFile))
                .andExpect(status().is(200))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assert (expectedResponse.equals(content));

    }

}