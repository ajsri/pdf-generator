package com.ajsrivastava;

import com.ajsrivastava.delegate.PDFDelegate;
import com.ajsrivastava.model.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GeneratorService {
    @RequestMapping("/pdf")
    public ResponseEntity<byte[]> convertPDF(@RequestParam("url") String url) {
        PDFDelegate gd = new PDFDelegate(url, "pdf-gen-temp", true);
        return gd.show();
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> s3Pdf(@RequestBody Request request) {
        Map<String, String> response = new HashMap<>();
        PDFDelegate gd = new PDFDelegate(request.getMarkup(), "pdf-gen-temp", false);
        response.put("url", gd.save(request.getFilename()));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
