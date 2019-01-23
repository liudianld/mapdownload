package com.shenlandt.wh.providers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.internals.PureImageProxy;
import com.shenlandt.wh.internals.impl.GMapImage;
import com.shenlandt.wh.internals.impl.GMapImageProxy;
import com.shenlandt.wh.pojo.GMaps;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.pojo.RectLatLng;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * 每个map provider的基类
 */
public abstract class GMapProvider {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GMapProvider.class);
    /**
     * provider name
     */
    public abstract String getName();
    
    /**
     * provider type
     * @return
     */
    public abstract TileTypeEnum getType();

    /**
     * provider projection
     */
    public abstract PureProjection getProjection();

    /**
     * provider overlays
     */
    public abstract GMapProvider[] getOverlays();
    
    public String Copyright = "";
    
    /**
     * 加载前初始化内容,可以放入版本相关内容
     */
    public void OnInitialized() {
    }

    /**
     * 使用实现的provider得到瓦片
     * 
     * @param pos
     * @param zoom
     * @return
     */
    public abstract PureImage GetTileImage(GPoint pos, int zoom) throws IOException;
    
    public abstract String GetTileImageUrl(GPoint pos, int zoom);
    
    private static final List<GMapProvider> MapProviders = new ArrayList<GMapProvider>();

    protected GMapProvider() {
        MapProviders.add(this);
    }

    private boolean isInitialized = false;

    /**
     * provider 是否初始化过了
     */
    public final boolean getIsInitialized() {
        return isInitialized;
    }

    public final void setIsInitialized(boolean value) {
        isInitialized = value;
    }

    /**
     * 地图区域
     */
    public RectLatLng Area;

    /**
     * 最小缩放级别
     */
    public int MinZoom;

    /**
     * 最大缩放级别
     */
    public Integer MaxZoom = 17;

    /**
     * provider 连接超时
     */
    public static int TimeoutMs = 5 * 1000;

    /**
     * 设置或取得参考的HTTP头的值
     */
    public String RefererUrl = "";

    /**
     * 如果瓦片从 BottomLeft 开始时为真, WMS-C
     */
    public boolean InvertedAxisY = false;

    /**
     * 图像管理内部代理
     */
    public static PureImageProxy TileImageProxy = new GMapImageProxy();
    
    public String tilePath;

    protected final PureImage GetTileImageUsingHttp(String url) throws IOException {
        PureImage ret = null;
        InputStream is = null;
        try {
            URL imageURL = new URL(url);
            is = imageURL.openStream();
            byte[] imageBytes = IOUtils.toByteArray(is);
            ret = TileImageProxy.FromArray(imageBytes);
        } catch (IOException e) {
            throw e;
        } finally {
            is.close();
        }

        return ret;
    }
    
    public String getCachePath(int zoom, GPoint point){
        long x = point.getX();
        long y = point.getY();
        String path = GMaps.Instance.tilePath+"/"+getName()+"/L" + zoom + "//R" + y + "//";
        String filePath = path + "C" + x + ".png";
        return filePath;
    }
    
    public void CacheImage(int zoom, GPoint point, byte[] data) throws IOException {
      GMapImage image = (GMapImage)this.GetTileImageUsingBytes(data);
        
      String filePath = this.getCachePath(zoom, point);
      
      image.Save(filePath, "png");
      
      LOGGER.info("生成缓存图片 位置在："+filePath);
    }
    
    public void CacheImage(String path, String url) throws IOException{
        GMapImage image = (GMapImage)GetTileImageUsingHttp(url);
        if(image==null)
            return;
        image.Save(path, "png");
        LOGGER.info("生成缓存图片 位置在："+path);
    }
    
    public final PureImage GetTileImageUsingBytes(byte[] data) throws IOException{
        return TileImageProxy.FromArray(data);
    }
    
    protected final String GetContentUsingHttp(String url) {
        return null;
    }

    protected PureImage GetTileImageFromArray(byte[] data) throws IOException {
        return TileImageProxy.FromArray(data);
    }

    protected static int GetServerNum(GPoint pos, int max) {
        return (int) (pos.getX() + 2 * pos.getY()) % max;
    }

    @Override
    public String toString() {
        return getName();
    }
}
