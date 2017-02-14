package com.ajsrivastava.delegate;

import com.lowagie.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

public class GeneratorDelegate {
    private String url;
    
    public GeneratorDelegate(String url) {
        this.url = url;
    }
    
    public ResponseEntity<byte[]> create() {
        try {
            final ITextRenderer iTextRenderer = new ITextRenderer();
            iTextRenderer.setDocument(this.url);
            iTextRenderer.layout();
        
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
        
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            iTextRenderer.createPDF(stream);
            return new ResponseEntity<>(stream.toByteArray(), headers, HttpStatus.OK);
        }
        catch (final DocumentException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
