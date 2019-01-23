package com.shenlandt.wh.providers.etc;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.pojo.RectLatLng;
import com.shenlandt.wh.pojo.TileTypeEnum;
import com.shenlandt.wh.projections.MercatorProjection;
import com.shenlandt.wh.providers.GMapProvider;
import com.shenlandt.wh.utils.DateTimeUtil;

/**
 * TurkeyMap provider, http: //maps.pergo.com.tr/
 */
public class TurkeyMapProvider extends GMapProvider {
    public static TurkeyMapProvider Instance = new TurkeyMapProvider();

    private TurkeyMapProvider() {
        Copyright = String.format(
                "©%1$s Pergo - Map data ©%1$s Fideltus Advanced Technology",
                DateTimeUtil.getDateExtract(new Date(), "y"));
        Area = new RectLatLng(42.5830078125, 25.48828125, 19.05029296875,
                6.83349609375);
        InvertedAxisY = true;
    }

    @Override
    public String getName() {
        return "TurkeyMap";
    }

    private GMapProvider[] overlays;

    @Override
    public GMapProvider[] getOverlays() {
        if (overlays == null) {
            overlays = new GMapProvider[] { this };
        }
        return overlays;
    }

    @Override
    public PureProjection getProjection() {
        return MercatorProjection.Instance;
    }

    @Override
    public PureImage GetTileImage(GPoint pos, int zoom) throws IOException {
        String url = MakeTileImageUrl(pos, zoom, "");

        return GetTileImageUsingHttp(url);
    }

    private String MakeTileImageUrl(GPoint pos, int zoom, String language) {
        // http://{domain}/{layerName}/{zoomLevel}/{first3LetterOfTileX}/{second3LetterOfTileX}/{third3LetterOfTileX}/{first3LetterOfTileY}/{second3LetterOfTileY}/{third3LetterOfTileXY}.png

        // http://map3.pergo.com.tr/tile/00/000/000/001/000/000/000.png
        // 意思是: Zoom Level: 0 TileX: 1 TileY: 0

        // http://domain/tile/14/000/019/371/000/011/825.png
        // 意思是: Zoom Level: 14 TileX: 19371 TileY:11825

        // 新版
        // http://map1.pergo.com.tr/publish/tile/tile9913/06/000/000/038/000/000/039.png

        StringBuffer x = new StringBuffer(String.format(Zeros, pos.getX()))
                .insert(3, Slash).insert(7, Slash); // - 000/000/001
        StringBuffer y = new StringBuffer(String.format(Zeros, pos.getY()))
                .insert(3, Slash).insert(7, Slash); // - 000/000/000

        return MessageFormat.format(UrlFormat, GetServerNum(pos, 3), zoom, x, y);
    }
    
    @Override
    public String GetTileImageUrl(GPoint pos, int zoom) {
        return MakeTileImageUrl(pos, zoom, "");
    }
    
    private static final String Zeros = "%09d"; // 000000000
    private static final String Slash = "/";
    private static final String UrlFormat = "http://map{0}.pergo.com.tr/publish/tile/tile9913/{1:00}/{2}/{3}.png";

    @Override
    public TileTypeEnum getType() {
        return TileTypeEnum.ROAD;
    }
}