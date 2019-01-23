package com.shenlandt.wh.providers.baidu;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * 百度普通地图
 * <p/>
 * <p>User: weiq
 * <p>Date: 2015年10月19日 上午9:34:16 
 * <p>Version: 1.0
 */
public class BaiduMapProvider extends BaiduMapProviderBase{
    
    public static BaiduMapProvider Instance = new BaiduMapProvider();
    
    @Override
    public String getName()
    {
        return "BaiduMap";
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
        long offsetX = (long) Math.pow(2, zoom);
        long offsetY = offsetX - 1;

        long numX = pos.getX() - offsetX;
        long numY = -pos.getY() + offsetY;

        zoom = zoom + 1;
        String x = (numX+"").replace("-", "M"); //负数'-',替换为M
        String y = (numY+"").replace("-", "M"); //负数'-',替换为M

        //原来：http://q3.baidu.com/it/u=x=721;y=209;z=12;v=014;type=web&fm=44
        //更新：http://online1.map.bdimg.com/tile/?qt=tile&x=23144&y=6686&z=17&styles=pl
        return MessageFormat.format(UrlFormat, (GetServerNum(pos, serverNum) + 1), x, y, zoom);
    }
    
    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }

    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.ROAD;
    }
}
