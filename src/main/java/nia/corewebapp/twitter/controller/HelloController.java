package nia.corewebapp.twitter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello(@RequestParam(required = false) String name,
                        ModelMap model){
        model.put("name", name != null ? name : "username");
        return "hello";
    }

}
