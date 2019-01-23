package com.shenlandt.wh.providers.tencent;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * QQ卫星地图
 * 
 * <p>
 * Version: 1.0
 */
public class TencentSatelliteMapProvider extends TencentMapProviderBase {
    public static TencentSatelliteMapProvider Instance = new TencentSatelliteMapProvider();

    public static final String UrlFormat = 
            "http://p{0}.map.gtimg.com/sateTiles/{1}/{2}/{3}/{4}_{5}.jpg";

    @Override
    public String getName() {
        return "TencentSatelliteMap";
    }
    
    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.SATELLITE;
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");
        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        long y = (long) (Math.pow(2, zoom) - 1 - pos.getY());
        
        long x16 = (long) Math.floor(pos.getX()/16);
        long y16 = (long) Math.floor((Math.pow(2, zoom) - 1 - pos.getY())/16);
        
        return MessageFormat.format(UrlFormat,
                (GetServerNum(pos, serverNum) + 1), 
                zoom,
                x16+"",
                y16+"",
                pos.getX()+"", 
                y+""
              );
    }

    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
}
