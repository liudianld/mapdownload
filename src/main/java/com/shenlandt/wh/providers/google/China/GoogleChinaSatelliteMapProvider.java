package com.shenlandt.wh.providers.google.China;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;
import com.shenlandt.wh.providers.google.GoogleMapProviderBase;

/**
 * GoogleChinaSatelliteMap provider
 */
public class GoogleChinaSatelliteMapProvider extends GoogleMapProviderBase {
    public static GoogleChinaSatelliteMapProvider Instance = new GoogleChinaSatelliteMapProvider();

    private GoogleChinaSatelliteMapProvider() {
        RefererUrl = String.format("http://ditu.%1$s/", ServerChina);
    }

    public String Version = "s@186";

    @Override
    public String getName() {
        return "GoogleChinaSatelliteMap";
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");

        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        String[] rstr = GetSecureWords(pos, "", "");

        return MessageFormat.format(UrlFormat, UrlFormatServer,
                GetServerNum(pos, 4), UrlFormatRequest, Version, pos.getX()
                        + "", rstr[0], pos.getY() + "", zoom, rstr[1], ServerChina);
    }
    
    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
    
    private static final String UrlFormatServer = "mt";
    private static final String UrlFormatRequest = "vt";
    private static final String UrlFormat = "http://{0}{1}.{9}/{2}/lyrs={3}&gl=cn&x={4}{5}&y={6}&z={7}&s={8}";

    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.SATELLITE;
    }
}