package com.ajsrivastava;

import com.lowagie.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

/**
 * Created by ASRIV1 on 2/14/17.
 */
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
        
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            iTextRenderer.createPDF(baos);
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        }
        catch (final DocumentException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
