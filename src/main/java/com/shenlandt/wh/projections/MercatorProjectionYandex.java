package com.shenlandt.wh.projections;

import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.GSize;
import com.shenlandt.wh.pojo.PointLatLng;
import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.pojo.RectLatLng;

public class MercatorProjectionYandex extends PureProjection {
    public static final MercatorProjectionYandex Instance = new MercatorProjectionYandex();

    private static final double MinLatitude = -85.05112878;
    private static final double MaxLatitude = 85.05112878;
    private static final double MinLongitude = -177;
    private static final double MaxLongitude = 177;

    private static final double RAD_DEG = 180 / Math.PI;
    private static final double DEG_RAD = Math.PI / 180;
    private static final double MathPiDiv4 = Math.PI / 4;

    @Override
    public RectLatLng getBounds() {
        return RectLatLng.FromLTRB(MinLongitude, MaxLatitude, MaxLongitude,
                MinLatitude);
    }

    private GSize tileSize = new GSize(256, 256);

    @Override
    public GSize getTileSize() {
        return tileSize;
    }

    @Override
    public double getAxis() {
        return 6356752.3142;
    }

    @Override
    public double getFlattening() {
        return (1.0 / 298.257223563);
    }

    @Override
    public GPoint FromLatLngToPixel(double lat, double lng, int zoom) {
        lat = Clip(lat, MinLatitude, MaxLatitude);
        lng = Clip(lng, MinLongitude, MaxLongitude);

        double rLon = lng * DEG_RAD; // Math.PI / 180;
        double rLat = lat * DEG_RAD; // Math.PI / 180;

        double a = 6378137;
        double k = 0.0818191908426;

        double z = Math.tan(MathPiDiv4 + rLat / 2)
                / Math.pow(
                        (Math.tan(MathPiDiv4 + Math.asin(k * Math.sin(rLat))
                                / 2)), k);
        double z1 = Math.pow(2, 23 - zoom);

        double DX = ((20037508.342789 + a * rLon) * 53.5865938 / z1);
        double DY = ((20037508.342789 - a * Math.log(z)) * 53.5865938 / z1);

        GPoint ret = GPoint.Empty;
        ret.setX((long) DX);
        ret.setY((long) DY);

        return ret;
    }

    @Override
    public PointLatLng FromPixelToLatLng(long x, long y, int zoom) {
        GSize s = GetTileMatrixSizePixel(zoom).clone();

        double mapSizeX = s.getWidth();
        double mapSizeY = s.getHeight();

        double a = 6378137;
        double c1 = 0.00335655146887969;
        double c2 = 0.00000657187271079536;
        double c3 = 0.00000001764564338702;
        double c4 = 0.00000000005328478445;
        double z1 = (23 - zoom);
        double mercX = (x * Math.pow(2, z1)) / 53.5865938 - 20037508.342789;
        double mercY = 20037508.342789 - (y * Math.pow(2, z1)) / 53.5865938;

        double g = Math.PI / 2 - 2 * Math.atan(1 / Math.exp(mercY / a));
        double z = g + c1 * Math.sin(2 * g) + c2 * Math.sin(4 * g) + c3
                * Math.sin(6 * g) + c4 * Math.sin(8 * g);

        PointLatLng ret = PointLatLng.Empty;
        ret.setLat(z * RAD_DEG);
        ret.setLng(mercX / a * RAD_DEG);

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