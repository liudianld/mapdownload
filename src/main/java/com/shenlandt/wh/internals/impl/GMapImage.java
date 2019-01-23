package com.shenlandt.wh.internals.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import com.google.common.io.Files;
import com.shenlandt.wh.internals.PureImage;

/**
 * map4j Image
 * 
 * <p>Version: 1.0
 */
public class GMapImage extends PureImage{
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public boolean Save(String filePath) {
        try {
            File file = new File(filePath);
            Files.createParentDirs(file);
            ImageIO.write(image, getFormat(Data), file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean Save(String filePath, String format) {
        try {
            File file = new File(filePath);
            if(file.exists()) //如果有缓存图片不会重复创建
                return true;
            Files.createParentDirs(file);
            ImageIO.write(image, format, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void main(String[] args) throws MalformedURLException, IOException {
        
    }
}
