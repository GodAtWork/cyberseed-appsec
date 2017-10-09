package edu.syr.cyberseed.sage.server.controllers.documentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class SwaggerDocumentation {
   // @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }

}