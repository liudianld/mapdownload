package com.shenlandt.wh.projections;

import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.GSize;
import com.shenlandt.wh.pojo.PointLatLng;
import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.pojo.RectLatLng;

/**
 * Plate Carrée (literally, "plane square") projection
 * PROJCS["WGS 84 / World Equidistant Cylindrical"
 * ,GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984"
 * ,SPHEROID["WGS_1984",6378137,298.257223563
 * ]],PRIMEM["Greenwich",0],UNIT["Degree"
 * ,0.017453292519943295]],UNIT["Meter",1]]
 * 
 * "spatialReference": {"wkid":4326},"singleFusedMapCache":true,"tileInfo":
 * {"rows":256,"cols":256,"dpi":96,"format":"PNG8","compressionQuality":0,
 * "origin":{"x":-400,"y":400},"spatialReference":{"wkid":4326},"lods":
 * 
 * [{"level":0,"resolution":0.0118973050291514,"scale":5000000},
 * {"level":1,"resolution":0.0059486525145757,"scale":2500000},
 * {"level":2,"resolution":0.00297432625728785,"scale":1250000},
 * {"level":3,"resolution":0.00118973050291514,"scale":500000},
 * {"level":4,"resolution":0.00059486525145757,"scale":250000},
 * {"level":5,"resolution":0.000356919150874542,"scale":150000},
 * {"level":6,"resolution":0.000178459575437271,"scale":75000},
 * {"level":7,"resolution":0.000118973050291514,"scale":50000},
 * {"level":8,"resolution":5.9486525145757E-05,"scale":25000},
 * {"level":9,"resolution":3.56919150874542E-05,"scale":15000},
 * {"level":10,"resolution":1.90356880466422E-05,"scale":8000},
 * {"level":11,"resolution":9.51784402332112E-06,"scale":4000},
 * {"level":12,"resolution":4.75892201166056E-06,"scale":2000}]},
 * 
 * "initialExtent":
 * {"xmin":42.1125196069871,"ymin":18.6650706214551,"xmax":65.698643558112
 * 4,"ymax":29.4472987133981,"spatialReference":{"wkid":4326}},
 * 
 * "fullExtent":
 * {"xmin":41.522866508209,"ymin":18.7071563263201,"xmax":66.2882966568906
 * ,"ymax":29.4052130085331,"spatialReference":{"wkid":4326}},
 * 
 * "units":"esriDecimalDegrees"
 */
public class PlateCarreeProjectionDarbAe extends PureProjection {
    public static final PlateCarreeProjectionDarbAe Instance = new PlateCarreeProjectionDarbAe();

    public static final double MinLatitude = 18.7071563263201;
    public static final double MaxLatitude = 29.4052130085331;
    public static final double MinLongitude = 41.522866508209;
    public static final double MaxLongitude = 66.2882966568906;

    private static final double orignX = -400;
    private static final double orignY = 400;

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

        double res = GetTileMatrixResolution(zoom);

        ret.setX((long) Math.floor((lng - orignX) / res));
        ret.setY((long) Math.floor((orignY - lat) / res));

        return ret;
    }

    @Override
    public PointLatLng FromPixelToLatLng(long x, long y, int zoom) {
        PointLatLng ret = PointLatLng.Empty;

        double res = GetTileMatrixResolution(zoom);

        ret.setLat(orignY - (y * res));
        ret.setLng((x * res) + orignX);

        return ret;
    }

    public static double GetTileMatrixResolution(int zoom) {
        double ret = 0;

        switch (zoom) {
        case 0: {
            ret = 0.0118973050291514;
        }
            break;

        case 1: {
            ret = 0.0059486525145757;
        }
            break;

        case 2: {
            ret = 0.00297432625728785;
        }
            break;

        case 3: {
            ret = 0.00118973050291514;
        }
            break;

        case 4: {
            ret = 0.00059486525145757;
        }
            break;

        case 5: {
            ret = 0.000356919150874542;
        }
            break;

        case 6: {
            ret = 0.000178459575437271;
        }
            break;

        case 7: {
            ret = 0.000118973050291514;
        }
            break;

        case 8: {
            ret = 5.9486525145757E-05;
        }
            break;

        case 9: {
            ret = 3.56919150874542E-05;
        }
            break;

        case 10: {
            ret = 1.90356880466422E-05;
        }
            break;

        case 11: {
            ret = 9.51784402332112E-06;
        }
            break;

        case 12: {
            ret = 4.75892201166056E-06;
        }
            break;
        }

        return ret;
    }

    @Override
    public double GetGroundResolution(int zoom, double latitude) {
        return GetTileMatrixResolution(zoom);
    }

    @Override
    public GSize GetTileMatrixMaxXY(int zoom) {
        GPoint maxPx = FromLatLngToPixel(MinLatitude, MaxLongitude, zoom)
                .clone();
        return new GSize(FromPixelToTileXY(maxPx).clone());
    }

    @Override
    public GSize GetTileMatrixMinXY(int zoom) {
        GPoint minPx = FromLatLngToPixel(MaxLatitude, MinLongitude, zoom)
                .clone();
        return new GSize(FromPixelToTileXY(minPx).clone());
    }
}