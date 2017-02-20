package com.ajsrivastava;

import com.ajsrivastava.delegate.GeneratorDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GeneratorService {
    @RequestMapping("/pdf")
    public ResponseEntity<byte[]> convertPDF(@RequestParam("url") String url) {
        GeneratorDelegate gd = new GeneratorDelegate(url, "pdf-gen-temp");
        return gd.create();
    }
    
    @RequestMapping("/save")
    public ResponseEntity s3Pdf(@RequestParam("url") String url, @RequestParam("name") String name) {
        Map<String, String> response = new HashMap<>();
        GeneratorDelegate gd = new GeneratorDelegate(url, "pdf-gen-temp");
        gd.save(name);
        return new ResponseEntity(HttpStatus.OK);
    }
}
