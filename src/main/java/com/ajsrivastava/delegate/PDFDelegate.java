package com.ajsrivastava.delegate;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.lowagie.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class PDFDelegate {
    private String input;
    private String bucket;
    private boolean url;
    
    public PDFDelegate(String content, String bucket, boolean url) {
        this.input = content;
        this.bucket = bucket;
        this.url = url;
    }
    
    public ByteArrayOutputStream createImage() {
        try {
            final ITextRenderer iTextRenderer = new ITextRenderer();
            SharedContext sharedContext = iTextRenderer.getSharedContext();
            sharedContext.setReplacedElementFactory(new ImageDelegate(iTextRenderer.getSharedContext().getReplacedElementFactory()));
            if(this.url) {
                iTextRenderer.setDocument(this.input);
            }
            else {
                iTextRenderer.setDocumentFromString(this.input);
            }
            iTextRenderer.layout();
    
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            iTextRenderer.createPDF(stream);
            return stream;
        }
        
        catch (final DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ResponseEntity<byte[]> show() {
        ByteArrayOutputStream stream = this.createImage();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        
        return new ResponseEntity<>(stream.toByteArray(), headers, HttpStatus.OK);
    }
    
    public String save(String filename) {
        byte[] buffer = this.createImage().toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        
        AmazonS3Client s3 = new AmazonS3Client();
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(buffer.length);
        
        s3.putObject(new PutObjectRequest(this.bucket, filename + ".pdf", is, meta));
        return s3.getResourceUrl(this.bucket, filename + ".pdf");
    }
    
    public String getInput() {
        return input;
    }
    
    public void setInput(String input) {
        this.input = input;
    }
    
    public boolean isUrl() {
        return url;
    }
    
    public void setUrl(boolean url) {
        this.url = url;
    }
    
    public String getBucket() {
        return bucket;
    }
    
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
