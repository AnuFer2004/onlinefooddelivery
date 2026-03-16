package com._163.fooddeliverysystem.onlinefooddeleverysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

    @GetMapping("/admin")
    public String getAdminPage() {
        // Forward the /admin URL to the static admin.html file
        return "forward:/admin.html";
    }


}

