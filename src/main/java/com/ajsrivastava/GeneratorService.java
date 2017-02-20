package com.ajsrivastava;

import com.ajsrivastava.delegate.PDFDelegate;
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
        PDFDelegate gd = new PDFDelegate(url, "pdf-gen-temp");
        return gd.create();
    }
    
    @RequestMapping(value = "/save")
    public ResponseEntity<Map<String, String>> s3Pdf(@RequestParam("url") String url, @RequestParam("name") String name) {
        Map<String, String> response = new HashMap<>();
        PDFDelegate gd = new PDFDelegate(url, "pdf-gen-temp");
        response.put("url", gd.save(name));
        
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
