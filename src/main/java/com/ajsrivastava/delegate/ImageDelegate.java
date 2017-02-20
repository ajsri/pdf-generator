package com.ajsrivastava.delegate;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.apache.commons.io.IOUtils;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//http://www.tothenew.com/blog/using-data-urls-for-embedding-images-in-flying-saucer-generated-pdfs/

public class ImageDelegate implements ReplacedElementFactory {
    
    private final ReplacedElementFactory superFactory;
    
    public ImageDelegate(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }
    
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();
        if(e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        if(nodeName.equals("img")) {
            String attribute = e.getAttribute("src");
            FSImage fsImage = null;
            try {
                fsImage = buildImage(attribute, uac);
            }
            catch (BadElementException exception) {
                exception.printStackTrace();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            
            if(fsImage != null) {
                if(cssWidth != -1 || cssHeight != -1) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }
    
    protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException, BadElementException {
        FSImage fsImage = null;
        //Check if embedded image
        if(srcAttr.startsWith("data:image/")) {
            String base64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
            byte[] decodedBytes = new sun.misc.BASE64Decoder().decodeBuffer(base64encoded);
            fsImage = new ITextFSImage(Image.getInstance(decodedBytes));
        }
        else {
            InputStream input = null;
            try {
                System.out.println(srcAttr);
                if(srcAttr.indexOf("http") > -1) {
                    input = new URL(srcAttr).openStream();
                }
                else {
                    input = new FileInputStream("src/main/resources/templates/" + srcAttr);
                }

                final byte[] bytes = IOUtils.toByteArray(input);
                final Image image = Image.getInstance(bytes);
                fsImage = new ITextFSImage(image);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fsImage;
    }
    
    public void remove(Element e) {}
    
    public void reset() {}
    
    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {}
    
}
