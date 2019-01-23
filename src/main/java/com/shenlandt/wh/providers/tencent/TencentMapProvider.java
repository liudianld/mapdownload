package com.shenlandt.wh.providers.tencent;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * QQ普通地图
 * <p>
 * Version: 1.0
 */
public class TencentMapProvider extends TencentMapProviderBase {
    public static TencentMapProvider Instance = new TencentMapProvider();

    public static final String UrlFormat = 
            "http://rt{0}.map.gtimg.com/realtimerender?z={1}&x={2}&y={3}&type=vector&style=0";

    @Override
    public String getName() {
        return "TencentMap";
    }
    
    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.ROAD;
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");
        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        long y = (long) (Math.pow(2, zoom) - 1 - pos.getY());
        return MessageFormat.format(UrlFormat,
                (GetServerNum(pos, serverNum) + 1), zoom, pos.getX()+"", y+"");
    }

    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
}
