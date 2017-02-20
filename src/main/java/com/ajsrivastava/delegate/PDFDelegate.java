package com.ajsrivastava.delegate;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
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
import java.util.HashMap;
import java.util.Map;

public class PDFDelegate {
    private String url;
    private String bucket;
    
    public PDFDelegate(String url, String bucket) {
        this.url = url;
        this.bucket = bucket;
    }
    
    public ResponseEntity<byte[]> create() {
        try {
            final ITextRenderer iTextRenderer = new ITextRenderer();
            SharedContext sharedContext = iTextRenderer.getSharedContext();
            sharedContext.setReplacedElementFactory(new ImageDelegate(iTextRenderer.getSharedContext().getReplacedElementFactory()));
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    public String save(String filename) {
        try {
            final ITextRenderer iTextRenderer = new ITextRenderer();
            SharedContext sharedContext = iTextRenderer.getSharedContext();
            sharedContext.setReplacedElementFactory(new ImageDelegate(iTextRenderer.getSharedContext().getReplacedElementFactory()));
            iTextRenderer.setDocument(this.url);
            iTextRenderer.layout();
        
        
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            iTextRenderer.createPDF(stream);
            
            byte[] buffer = stream.toByteArray();
            InputStream is = new ByteArrayInputStream(buffer);
    
            AmazonS3Client s3 = new AmazonS3Client();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(buffer.length);
            
            s3.putObject(new PutObjectRequest(this.bucket, filename + ".pdf", is, meta));
            
            return s3.getResourceUrl(this.bucket, filename + ".pdf");
        }
        catch (final DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getBucket() {
        return bucket;
    }
    
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
