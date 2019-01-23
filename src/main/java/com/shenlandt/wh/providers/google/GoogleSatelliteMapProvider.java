package com.shenlandt.wh.providers.google;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;

/**
 * GoogleSatelliteMap provider
 */
public class GoogleSatelliteMapProvider extends GoogleMapProviderBase {
    public static GoogleSatelliteMapProvider Instance = new GoogleSatelliteMapProvider();

    private GoogleSatelliteMapProvider() {
    }

    public String Version = "181";

    @Override
    public String getName() {
        return "GoogleSatelliteMap";
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");

        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        String[] rstr = GetSecureWords(pos, "", "");

        return MessageFormat.format(UrlFormat, UrlFormatServer,
                GetServerNum(pos, 4), UrlFormatRequest, Version, language,
                pos.getX() + "", rstr[0], pos.getY() + "", zoom, rstr[1], Server);
    }

    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
    
    private static final String UrlFormatServer = "khm";
    private static final String UrlFormatRequest = "kh";
    private static final String UrlFormat = "http://{0}{1}.{10}/{2}/v={3}&hl={4}&x={5}{6}&y={7}&z={8}&s={9}";

    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.SATELLITE;
    }
}