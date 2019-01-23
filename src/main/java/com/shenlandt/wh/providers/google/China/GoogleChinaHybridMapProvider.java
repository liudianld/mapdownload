package com.shenlandt.wh.providers.google.China;

import java.io.IOException;
import java.text.MessageFormat;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.TileTypeEnum;
import com.shenlandt.wh.providers.GMapProvider;
import com.shenlandt.wh.providers.google.GoogleMapProviderBase;

/**
 * GoogleChinaHybridMap provider
 */
public class GoogleChinaHybridMapProvider extends GoogleMapProviderBase {
    public static GoogleChinaHybridMapProvider Instance = new GoogleChinaHybridMapProvider();

    private GoogleChinaHybridMapProvider() {
        RefererUrl = String.format("http://ditu.%1$s/", ServerChina);
    }

    public String Version = "y@186";

    @Override
    public String getName() {
        return "GoogleChinaHybridMap";
    }

    private GMapProvider[] overlays;

    @Override
    public GMapProvider[] getOverlays() {
        if (overlays == null) {
            overlays = new GMapProvider[] {
                    GoogleChinaSatelliteMapProvider.Instance, this };
        }
        return overlays;
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");

        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        String[] rstr = GetSecureWords(pos, "", "");

        return MessageFormat
                .format(UrlFormat, UrlFormatServer, GetServerNum(pos, 4),
                        UrlFormatRequest, Version, ChinaLanguage, pos.getX()
                                + "", rstr[0], pos.getY() + "", zoom, rstr[1],
                        ServerChina);
    }
    
    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
    
    private static final String ChinaLanguage = "zh-CN";
    private static final String UrlFormatServer = "mt";
    private static final String UrlFormatRequest = "vt";
    private static final String UrlFormat = "http://{0}{1}.{10}/{2}/imgtp=png32&lyrs={3}&hl={4}&gl=cn&x={5}{6}&y={7}&z={8}&s={9}";

    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.HYBRID;
    }
}