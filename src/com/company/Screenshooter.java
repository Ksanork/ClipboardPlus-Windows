package com.company;


import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mikołaj on 2016-02-28.
 */
public class Screenshooter {
    private BufferedImage screencapture = null;

    public Screenshooter() {}

    public void makeScreenshot() {
        //BufferedImage screencapture = null;
        try {
            screencapture = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()) );
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public String getBase64() {
        String result = "";

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(screencapture, "PNG", out);
            byte[] bytes = out.toByteArray();

            result = Base64.encodeBase64String(bytes);
            //String base64bytes = Base64.encode(bytes);
            //String src = "data:image/png;base64," + base64bytes;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void saveAsJPG(String name) {
        File file = new File(name);
        try {
            ImageIO.write(screencapture, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
