package com.shenlandt.wh.internals.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.internals.PureImageProxy;

/**
 * GMapImageProxy
 * 
 * <p>Version: 1.0
 */
public class GMapImageProxy extends PureImageProxy {
    
    @Override
    public PureImage FromStream(ByteArrayInputStream stream) {
        GMapImage ret = null;
        try {
            ret = new GMapImage();
            ret.Data = stream;

            BufferedImage image = ImageIO.read(stream);
            ret.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
