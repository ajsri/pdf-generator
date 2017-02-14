package com.ajsrivastava;

import com.ajsrivastava.delegate.GeneratorDelegate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneratorService {
    @RequestMapping("/pdf")
    public ResponseEntity<byte[]> convertPDF(@RequestParam("url") String url) {
        GeneratorDelegate gd = new GeneratorDelegate(url);
        return gd.create();
    }
}
