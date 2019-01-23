package com.shenlandt.wh.pojo;

import java.util.Arrays;
import java.util.List;

/**
 * Polygon
 * 
 * <p/>
 * <p>Version: 1.0
 */
public class GMapPolygon {
    private List<PointLatLng> points;
    
    public GMapPolygon() {
    }
    
    public GMapPolygon(PointLatLng... p) {
        points = Arrays.asList(p);
    }

    public List<PointLatLng> getPoints() {
        return points;
    }

    public void setPoints(List<PointLatLng> points) {
        this.points = points;
    };
}
