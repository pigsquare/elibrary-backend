package db2.elibrary.controller;

import db2.elibrary.dto.AddUserTestDto;
import db2.elibrary.entity.User;
import db2.elibrary.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("test")
public class TestController {

    @RequestMapping("/hello/{name}")
    public String Test(Model model, @PathVariable String name){
        model.addAttribute("name1",name);
        return "/test";
    }

}
