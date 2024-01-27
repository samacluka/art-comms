package com.luka2.comms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/images")
public class ImageController {

    @GetMapping
    public byte[] getImage(@RequestParam Long id) {
        return new byte[]{};
    }

}
