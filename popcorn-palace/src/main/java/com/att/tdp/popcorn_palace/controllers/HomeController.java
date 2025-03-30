package com.att.tdp.popcorn_palace.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> Hello() {
        return ResponseEntity.ok().body("Popcorn palace Liran Napadenski");
    }
    
}
