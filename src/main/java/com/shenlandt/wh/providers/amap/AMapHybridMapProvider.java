package com.shenlandt.wh.providers.amap;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * 高德路网地图
 * 
 * Version: 1.0
 */
public class AMapHybridMapProvider extends AMapProviderBase {
    public static AMapHybridMapProvider Instance = new AMapHybridMapProvider();

    public static final String UrlFormat = 
    		"http://wprd0{0}.is.autonavi.com/appmaptile?x={1}&y={2}&z={3}&lang=zh_cn&size=1&scl=1&style=8";

    @Override
    public String getName() {
        return "AMapHybridMap";
    }
    
    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.HYBRID;
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");
        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        
        return MessageFormat.format(UrlFormat,
                (GetServerNum(pos, serverNum) + 1), pos.getX()+"", pos.getY()+"", zoom);
    }

    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
}
