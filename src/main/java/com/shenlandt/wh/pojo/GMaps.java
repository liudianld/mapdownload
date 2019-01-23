package com.shenlandt.wh.pojo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.internals.impl.GMapImage;
import com.shenlandt.wh.providers.GMapProvider;
import com.shenlandt.wh.utils.asynchttp.AsyncHttpConnection;

/**
 * maps manager
 */
public class GMaps {
    private static final Logger LOGGER = LoggerFactory.getLogger(GMaps.class);
    
    public static final GMaps Instance = new GMaps();
    
    public static final AsyncHttpConnection http = AsyncHttpConnection.getInstance();
    
    public static GMaps getInstance() {
        return Instance;
    }

    public String tilePath;
    private GMaps() {
        tilePath = "";
    }
    
    /**
     * 从瓦片服务中得到图片
     * 
     * @param provider
     * @param pos
     * @param zoom
     * @return
     */
    public final PureImage GetImageFrom(GMapProvider provider, GPoint pos, int zoom) {
        PureImage ret = null;

        if (ret == null) {
            try {
                ret = provider.GetTileImage(pos, zoom);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
    
    public final boolean saveLR(int zoom, GPoint point, PureImage image){
        GMapImage gimage = (GMapImage)image;
//        String str1 = String.format("%02d", zoom);
        long x = point.getX();
        long y = point.getY();
//        String str2 = String.format("%08x", x);
//        String str3 = String.format("%08x", y);
        
        String str1 = zoom+"";
        String str2 = x+"";
        String str3 = y+"";
        
        String path = "D://GisMap//L" + str1 + "//R" + str3 + "//";
        String filePath = path + "C" + str2 + ".png";
        
        gimage.Save(filePath, "png");
        return false;
    }
}