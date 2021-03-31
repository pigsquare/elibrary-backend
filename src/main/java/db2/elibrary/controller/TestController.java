package db2.elibrary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {
    @RequestMapping("/hello/{name}")
    public String Test(Model model, @PathVariable String name){
        model.addAttribute("name1",name);
        return "/test";
    }
}
