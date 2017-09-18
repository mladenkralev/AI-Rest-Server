package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String returnHome(HttpServletResponse response) {
        response.setHeader("Content-Type","text/html");
        return "index.html";
    }
}
