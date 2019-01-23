package com.shenlandt.wh.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * PureProjection 所有Projection父类
 * 
 * <p>Version: 1.0
 */
public abstract class PureProjection {
    
    private final java.util.ArrayList<java.util.HashMap<PointLatLng, GPoint>> FromLatLngToPixelCache = 
            new java.util.ArrayList<java.util.HashMap<PointLatLng, GPoint>>(33);
    private final java.util.ArrayList<java.util.HashMap<GPoint, PointLatLng>> FromPixelToLatLngCache = 
            new java.util.ArrayList<java.util.HashMap<GPoint, PointLatLng>>(33);

    public PureProjection() {
        for (int i = 0; i < FromLatLngToPixelCache.size(); i++) {
            FromLatLngToPixelCache
                    .add(new java.util.HashMap<PointLatLng, GPoint>());
            FromPixelToLatLngCache
                    .add(new java.util.HashMap<GPoint, PointLatLng>());
        }
    }

    /**
     * 瓦片大小
     */
    public abstract GSize getTileSize();

    /**
     * 椭圆体的半长轴，米
     */
    public abstract double getAxis();

    /**
     * Flattening of ellipsoid
     */
    public abstract double getFlattening();

    /**
     * 经纬度坐标转像素坐标
     * 
     * @param lat
     * @param lng
     * @param zoom
     * @return
     */
    public abstract GPoint FromLatLngToPixel(double lat, double lng, int zoom);

    /**
     * 像素坐标转经纬度坐标
     * 
     * @param x
     * @param y
     * @param zoom
     * @return
     */
    public abstract PointLatLng FromPixelToLatLng(long x, long y, int zoom);

    public final GPoint FromLatLngToPixel(PointLatLng p, int zoom) {
        return FromLatLngToPixel(p, zoom, false);
    }

    /**
     * 经纬度坐标转像素坐标
     * 
     * @param p
     * @param zoom
     * @return
     */
    public final GPoint FromLatLngToPixel(PointLatLng p, int zoom, boolean useCache) {
        if (useCache) {
            GPoint ret = GPoint.Empty;
            if (!((ret = FromLatLngToPixelCache.get(zoom).get(p)) != null)) {
                ret = FromLatLngToPixel(p.getLat(), p.getLng(), zoom).clone();
                FromLatLngToPixelCache.get(zoom).put(p, ret);

                // 缓存
                if (!FromPixelToLatLngCache.get(zoom).containsKey(ret)) {
                    FromPixelToLatLngCache.get(zoom).put(ret, p);
                }
            }
            return ret;
        } else {
            return FromLatLngToPixel(p.getLat(), p.getLng(), zoom);
        }
    }

    /**
     * 像素坐标转经纬度坐标
     * @param p
     * @param zoom
     * @return
     */
    public final PointLatLng FromPixelToLatLng(GPoint p, int zoom) {
        return FromPixelToLatLng(p, zoom, false);
    }

    /**
     * 像素坐标转经纬度坐标
     * 
     * @param p
     * @param zoom
     * @return
     */
    public final PointLatLng FromPixelToLatLng(GPoint p, int zoom,
            boolean useCache) {
        if (useCache) {
            PointLatLng ret = PointLatLng.Empty;
            if (!((ret = FromPixelToLatLngCache.get(zoom).get(p)) != null)) {
                ret = FromPixelToLatLng(p.getX(), p.getY(), zoom).clone();
                FromPixelToLatLngCache.get(zoom).put(p, ret);

                // 缓存
                if (!FromLatLngToPixelCache.get(zoom).containsKey(ret)) {
                    FromLatLngToPixelCache.get(zoom).put(ret, p);
                }
            }
            return ret;
        } else {
            return FromPixelToLatLng(p.getX(), p.getY(), zoom);
        }
    }

    /**
     * 像素坐标转瓦片坐标
     * 
     * @param p
     * @return
     */
    public GPoint FromPixelToTileXY(GPoint p) {
        return new GPoint((long) (p.getX() / getTileSize().getWidth()),
                (long) (p.getY() / getTileSize().getHeight()));
    }

    /**
     * 瓦片坐标转像素坐标
     * 
     * @param p
     * @return
     */
    public GPoint FromTileXYToPixel(GPoint p) {
        return new GPoint((p.getX() * getTileSize().getWidth()),
                (p.getY() * getTileSize().getHeight()));
    }

    /**
     * 自定义缩放级别下的最小瓦片大小
     * 
     * @param zoom
     * @return
     */
    public abstract GSize GetTileMatrixMinXY(int zoom);

    /**
     * 自定义缩放级别下的最大瓦片大小
     * 
     * @param zoom
     * @return
     */
    public abstract GSize GetTileMatrixMaxXY(int zoom);

    /**
     * 获取瓦片矩阵大小
     * 
     * @param zoom
     * @return
     */
    public GSize GetTileMatrixSizeXY(int zoom) {
        GSize sMin = GetTileMatrixMinXY(zoom).clone();
        GSize sMax = GetTileMatrixMaxXY(zoom).clone();

        return new GSize(sMax.getWidth() - sMin.getWidth() + 1,
                sMax.getHeight() - sMin.getHeight() + 1);
    }

    /**
     * 在自定义缩放级别的像素瓦片矩阵大小
     * 
     * @param zoom
     * @return
     */
    public final long GetTileMatrixItemCount(int zoom) {
        GSize s = GetTileMatrixSizeXY(zoom).clone();
        return (s.getWidth() * s.getHeight());
    }

    /**
     * 获取矩阵大小 像素
     * 
     * @param zoom
     * @return
     */
    public GSize GetTileMatrixSizePixel(int zoom) {
        GSize s = GetTileMatrixSizeXY(zoom).clone();
        return new GSize(s.getWidth() * getTileSize().getWidth(), s.getHeight()
                * getTileSize().getHeight());
    }

    /**
     * 获取所有的矩形范围内的瓦片，在特定缩放级别下
     */
    public final List<GPoint> GetAreaTileList(RectLatLng rect, int zoom, int padding) {
        List<GPoint> ret = new ArrayList<GPoint>();

        GPoint topLeft = FromPixelToTileXY(
                FromLatLngToPixel(rect.getLocationTopLeft(), zoom).clone())
                .clone();
        GPoint rightBottom = FromPixelToTileXY(
                FromLatLngToPixel(rect.getLocationRightBottom(), zoom).clone())
                .clone();

        for (long x = (topLeft.getX() - padding); x <= (rightBottom.getX() + padding); x++) {
            for (long y = (topLeft.getY() - padding); y <= (rightBottom.getY() + padding); y++) {
                GPoint p = new GPoint(x, y);
                if (!ret.contains(p) && p.getX() >= 0 && p.getY() >= 0) {
                    ret.add(p);
                }
            }
        }

        return ret;
    }

    /**
     * The ground resolution indicates the distance (in meters) on the ground
     * that’s represented by a single pixel in the map. For example, at a ground
     * resolution of 10 meters/pixel, each pixel represents a ground distance of
     * 10 meters.
     * 
     * @param zoom
     * @param latitude
     * @return
     */
    public double GetGroundResolution(int zoom, double latitude) {
        return (Math.cos(latitude * (Math.PI / 180)) * 2 * Math.PI * getAxis())
                / GetTileMatrixSizePixel(zoom).getWidth();
    }

    /**
     * 取得范围
     */
    public RectLatLng getBounds() {
        return RectLatLng.FromLTRB(-180, 90, 180, -90);
    }

    /**
     * PI
     */
    protected static final double PI = Math.PI;

    /**
     * Half of PI
     */
    protected static final double HALF_PI = (PI * 0.5);

    /**
     * PI * 2
     */
    protected static final double TWO_PI = (PI * 2.0);

    /**
     * EPSLoN
     */
    protected static final double EPSLoN = 1.0e-10;

    /**
     * MAX_VAL
     */
    protected static final double MAX_VAL = 4;

    /**
     * MAXLONG
     */
    protected static final double MAXLONG = 2147483647;

    /**
     * DBLLONG
     */
    protected static final double DBLLONG = 4.61168601e18;

    private static final double R2D = 180 / Math.PI; 
    private static final double D2R = Math.PI / 180;

    public static double DegreesToRadians(double deg) {
        return (D2R * deg);
    }

    public static double RadiansToDegrees(double rad) {
        return (R2D * rad);
    }

    /**
     * 反回变量符号
     */
    protected static double Sign(double x) {
        if (x < 0.0) {
            return (-1);
        } else {
            return (1);
        }
    }

    protected static double AdjustLongitude(double x) {
        long count = 0;
        while (true) {
            if (Math.abs(x) <= PI) {
                break;
            } else {
                if (((long) Math.abs(x / Math.PI)) < 2) {
                    x = x - (Sign(x) * TWO_PI);
                } else {
                    if (((long) Math.abs(x / TWO_PI)) < MAXLONG) {
                        x = x - (((long) (x / TWO_PI)) * TWO_PI);
                    } else {
                        if (((long) Math.abs(x / (MAXLONG * TWO_PI))) < MAXLONG) {
                            x = x
                                    - (((long) (x / (MAXLONG * TWO_PI))) * (TWO_PI * MAXLONG));
                        } else {
                            if (((long) Math.abs(x / (DBLLONG * TWO_PI))) < MAXLONG) {
                                x = x
                                        - (((long) (x / (DBLLONG * TWO_PI))) * (TWO_PI * DBLLONG));
                            } else {
                                x = x - (Sign(x) * TWO_PI);
                            }
                        }
                    }
                }
            }
            count++;
            if (count > MAX_VAL) {
                break;
            }
        }
        return (x);
    }

    /**
     * 计算正弦和余弦
     */
    protected static double[] SinCos(double val) {
        double[] r = {Math.sin(val),Math.cos(val)};
        return r;
    }
    
    /**
     * computes the constants e0, e1, e2, and e3 which are used in a series for
     * calculating the distance along a meridian.
     * 
     * @param x 表示，偏心率
     * @return
     */
    protected static double e0fn(double x) {
        return (1.0 - 0.25 * x * (1.0 + x / 16.0 * (3.0 + 1.25 * x)));
    }

    protected static double e1fn(double x) {
        return (0.375 * x * (1.0 + 0.25 * x * (1.0 + 0.46875 * x)));
    }

    protected static double e2fn(double x) {
        return (0.05859375 * x * x * (1.0 + 0.75 * x));
    }

    protected static double e3fn(double x) {
        return (x * x * x * (35.0 / 3072.0));
    }

    /**
     * computes the value of M which is the distance along a meridian from the
     * Equator to latitude phi.
     */
    protected static double mlfn(double e0, double e1, double e2, double e3,
            double phi) {
        return (e0 * phi - e1 * Math.sin(2.0 * phi) + e2 * Math.sin(4.0 * phi) - e3
                * Math.sin(6.0 * phi));
    }

    /**
     * calculates UTM zone number
     * 
     * @param lon 经度
     * @return
     */
    protected static long GetUTMzone(double lon) {
        return ((long) (((lon + 180.0) / 6.0) + 1.0));
    }

    /**
     * Clips a number to the specified minimum and maximum values.
     * 
     * @param n
     *            The number to clip.
     * @param minValue
     *            Minimum allowable value.
     * @param maxValue
     *            Maximum allowable value.
     * @return The clipped value.
     */
    protected static double Clip(double n, double minValue, double maxValue) {
        return Math.min(Math.max(n, minValue), maxValue);
    }

    /**
     * distance (in km) between two points specified by latitude/longitude The
     * Haversine formula, http://www.movable-type.co.uk/scripts/latlong.html
     * 
     * @param p1
     * @param p2
     * @return
     */
    public final double GetDistance(PointLatLng p1, PointLatLng p2) {
        double dLat1InRad = p1.getLat() * (Math.PI / 180);
        double dLong1InRad = p1.getLng() * (Math.PI / 180);
        double dLat2InRad = p2.getLat() * (Math.PI / 180);
        double dLong2InRad = p2.getLng() * (Math.PI / 180);
        double dLongitude = dLong2InRad - dLong1InRad;
        double dLatitude = dLat2InRad - dLat1InRad;
        double a = Math.pow(Math.sin(dLatitude / 2), 2) + Math.cos(dLat1InRad)
                * Math.cos(dLat2InRad) * Math.pow(Math.sin(dLongitude / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dDistance = (getAxis() / 1000.0) * c;
        return dDistance;
    }

    public final double GetDistanceInPixels(GPoint point1, GPoint point2) {
        double a = (double) (point2.getX() - point1.getX());
        double b = (double) (point2.getY() - point1.getY());

        return Math.sqrt(a * a + b * b);
    }

    /**
     * Accepts two coordinates in degrees.
     * 
     * @return A double value in degrees. From 0 to 360.
     */
    public final double GetBearing(PointLatLng p1, PointLatLng p2) {
        double latitude1 = DegreesToRadians(p1.getLat());
        double latitude2 = DegreesToRadians(p2.getLat());
        double longitudeDifference = DegreesToRadians(p2.getLng() - p1.getLng());

        double y = Math.sin(longitudeDifference) * Math.cos(latitude2);
        double x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1)
                * Math.cos(latitude2) * Math.cos(longitudeDifference);

        return (RadiansToDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    /**
     * Conversion from cartesian earth-sentered coordinates to geodetic
     * coordinates in the given datum
     * 
     * @param Lat
     * @param Lon
     * @param Height
     *            Height above ellipsoid [m]
     * @param X
     * @param Y
     * @param Z
     */
    public final void FromGeodeticToCartesian(double Lat, double Lng,
            double Height, double X, double Y,
            double Z) {
        Lat = (Math.PI / 180) * Lat;
        Lng = (Math.PI / 180) * Lng;

        double B = getAxis() * (1.0 - getFlattening());
        double ee = 1.0 - (B / getAxis()) * (B / getAxis());
        double N = (getAxis() / Math.sqrt(1.0 - ee * Math.sin(Lat)
                * Math.sin(Lat)));

        X = (N + Height) * Math.cos(Lat) * Math.cos(Lng);
        Y = (N + Height) * Math.cos(Lat) * Math.sin(Lng);
        Z = (N * (B / getAxis()) * (B / getAxis()) + Height)
                * Math.sin(Lat);
    }

    /**
     * Conversion from cartesian earth-sentered coordinates to geodetic
     * coordinates in the given datum
     * 
     * @param X
     * @param Y
     * @param Z
     * @param Lat
     * @param Lon
     */
    public final void FromCartesianTGeodetic(double X, double Y, double Z,
            double Lat, double Lng) {
        double E = getFlattening() * (2.0 - getFlattening());
        Lng = Math.atan2(Y, X);

        double P = Math.sqrt(X * X + Y * Y);
        double Theta = Math.atan2(Z, (P * (1.0 - getFlattening())));
        double st = Math.sin(Theta);
        double ct = Math.cos(Theta);
        Lat = Math.atan2(Z + E / (1.0 - getFlattening()) * getAxis()
                * st * st * st, P - E * getAxis() * ct * ct * ct);

        Lat /= (Math.PI / 180);
        Lng /= (Math.PI / 180);
    }
}