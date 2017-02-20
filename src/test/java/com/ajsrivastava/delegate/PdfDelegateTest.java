package com.ajsrivastava.delegate;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PdfDelegateTest {
    
    @Test
    public void init() {
        String url = "http://www.four7.us/example.html";
        String bucket = "pdf-gen-test";
        PDFDelegate gd = new PDFDelegate(url, bucket);
        assertSame("GD URL matches URL passed", url, gd.getUrl());
        assertSame("GD bucket matches bucket passed", bucket, gd.getBucket());
    }
    
    @Test
    public void create() {
        String url = "http://www.four7.us/example.html";
        String bucket = "pdf-gen-test";
        PDFDelegate gd = new PDFDelegate(url, bucket);
        
        gd.create();
        //add matchers lol
    }
    
    @Test
    public void save() {
        String url = "http://www.four7.us/example.html";
        String bucket = "pdf-gen-test";
        PDFDelegate gd = new PDFDelegate(url, bucket);
        gd.save("test");
    }
}