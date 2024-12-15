package com.profile.profilehama.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class RedirectController {
    @RequestMapping(value = "/**")
    public String redirect() {
        return "redirect:https://localhost:8089";
    }
}
