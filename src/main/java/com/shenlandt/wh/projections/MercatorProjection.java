package com.shenlandt.wh.projections;

import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.GSize;
import com.shenlandt.wh.pojo.PointLatLng;
import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.pojo.RectLatLng;

/** 
The Mercator projection
PROJCS["World_Mercator",GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984",SPHEROID["WGS_1984",6378137,298.257223563]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Mercator"],PARAMETER["False_Easting",0],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",0],PARAMETER["standard_parallel_1",0],UNIT["Meter",1]]
*/
public class MercatorProjection extends PureProjection {
    public static final MercatorProjection Instance = new MercatorProjection();

    private static final double MinLatitude = -85.05112878;
    private static final double MaxLatitude = 85.05112878;
    private static final double MinLongitude = -180;
    private static final double MaxLongitude = 180;

    @Override
    public RectLatLng getBounds() {
        return RectLatLng.FromLTRB(MinLongitude, MaxLatitude, MaxLongitude,
                MinLatitude);
    }

    private final GSize tileSize = new GSize(256, 256);

    @Override
    public GSize getTileSize() {
        return tileSize;
    }

    @Override
    public double getAxis() {
        return 6378137;
    }

    @Override
    public double getFlattening() {
        return (1.0 / 298.257223563);
    }

    @Override
    public GPoint FromLatLngToPixel(double lat, double lng, int zoom) {
        GPoint ret = GPoint.Empty;

        lat = Clip(lat, MinLatitude, MaxLatitude);
        lng = Clip(lng, MinLongitude, MaxLongitude);

        double x = (lng + 180) / 360;
        double sinLatitude = Math.sin(lat * Math.PI / 180);
        double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude))
                / (4 * Math.PI);

        GSize s = GetTileMatrixSizePixel(zoom).clone();
        long mapSizeX = s.getWidth();
        long mapSizeY = s.getHeight();

        ret.setX((long) Clip(x * mapSizeX + 0.5, 0, mapSizeX - 1));
        ret.setY((long) Clip(y * mapSizeY + 0.5, 0, mapSizeY - 1));

        return ret;
    }

    @Override
    public PointLatLng FromPixelToLatLng(long x, long y, int zoom) {
        PointLatLng ret = PointLatLng.Empty;

        GSize s = GetTileMatrixSizePixel(zoom).clone();
        double mapSizeX = s.getWidth();
        double mapSizeY = s.getHeight();

        double xx = (Clip(x, 0, mapSizeX - 1) / mapSizeX) - 0.5;
        double yy = 0.5 - (Clip(y, 0, mapSizeY - 1) / mapSizeY);

        ret.setLat(90 - 360 * Math.atan(Math.exp(-yy * 2 * Math.PI)) / Math.PI);
        ret.setLng(360 * xx);

        return ret;
    }

    @Override
    public GSize GetTileMatrixMinXY(int zoom) {
        return new GSize(0, 0);
    }

    @Override
    public GSize GetTileMatrixMaxXY(int zoom) {
        long xy = (1 << zoom);
        return new GSize(xy - 1, xy - 1);
    }
}