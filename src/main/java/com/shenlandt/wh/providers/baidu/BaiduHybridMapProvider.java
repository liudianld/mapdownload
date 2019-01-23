package com.shenlandt.wh.providers.baidu;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * 百度混合地图
 * <p/>
 * <p>User: weiq
 * <p>Date: 2015年10月19日 上午9:34:16 
 * <p>Version: 1.0
 */
public class BaiduHybridMapProvider extends BaiduMapProviderBase{

    public static BaiduHybridMapProvider Instance = new BaiduHybridMapProvider();
    
    @Override
    public String getName()
    {
        return "BaiduHybridMap";
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException
    {
        String url = MakeTileImageUrl(pos, zoom, "");
        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language)
    {
        
        zoom = zoom - 1;
        double offsetX = Math.pow(2, zoom);
        double offsetY = offsetX - 1;

        double numX = pos.getX() - offsetX;
        double numY = -pos.getY() + offsetY;

        zoom = zoom + 1;
        String x = (numX+"").replace("-", "M"); //负数'-',替换为M
        String y = (numY+"").replace("-", "M"); //负数'-',替换为M

        return MessageFormat.format(UrlFormat, (GetServerNum(pos, serverNum) + 1), x, y, zoom);
    }
    
    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
    
    private static final String UrlFormat = "http://online{0}.map.bdimg.com/tile/?qt=tile&x={1}&y={2}&z={3}&styles=sl&v=039&udt=20150601";

    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.HYBRID;
    }
}
