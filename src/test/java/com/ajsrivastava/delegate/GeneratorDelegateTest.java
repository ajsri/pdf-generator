package com.ajsrivastava.delegate;

import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratorDelegateTest {

    @Test
    public void create() {
        String url = "http://www.four7.us/example.html";
        PDFDelegate gd = new PDFDelegate(url, "pdf-gen-test");
        assertSame("GD URL matches URL passed", url, gd.getUrl());
    }
}