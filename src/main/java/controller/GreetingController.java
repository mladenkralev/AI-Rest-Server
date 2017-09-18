package controller;

import model.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import utll.Constants;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by mladen on 7/24/2017.
 */
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /**
     * Used for simple request/response testing.
     *
     * @param name passed parameter to url, to print different message
     * @return Greeting object representin Hello world and counter that is returned to the user
     */
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletResponse response) throws IOException {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}

