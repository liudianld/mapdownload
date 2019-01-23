package com.shenlandt.wh.pojo;

/**
 * 距形
 */
public class RectLatLng {
    public static RectLatLng Empty = new RectLatLng();
    private double lng;
    private double lat;
    private double widthLng;
    private double heightLat;

    public RectLatLng() {
    }

    public RectLatLng(double lat, double lng, double widthLng, double heightLat) {
        this.lng = lng;
        this.lat = lat;
        this.widthLng = widthLng;
        this.heightLat = heightLat;
        NotEmpty = true;
    }

    public RectLatLng(PointLatLng location, SizeLatLng size) {
        this.lng = location.getLng();
        this.lat = location.getLat();
        this.widthLng = size.getWidthLng();
        this.heightLat = size.getHeightLat();
        NotEmpty = true;
    }

    public static RectLatLng FromLTRB(double leftLng, double topLat, double rightLng, double bottomLat) {
        return new RectLatLng(topLat, leftLng, rightLng - leftLng, topLat - bottomLat);
    }

    public PointLatLng getLocationTopLeft() {
        return new PointLatLng(this.getLat(), this.getLng());
    }

    public void setLocationTopLeft(PointLatLng value) {
        this.setLng(value.getLng());
        this.setLat(value.getLat());
    }

    public PointLatLng getLocationRightBottom() {
        PointLatLng ret = new PointLatLng(this.getLat(), this.getLng());
        ret.Offset(getHeightLat(), getWidthLng());
        return ret;
    }

    public PointLatLng getLocationMiddle() {
        PointLatLng ret = new PointLatLng(this.getLat(), this.getLng());
        ret.Offset(getHeightLat() / 2, getWidthLng() / 2);
        return ret;
    }

    public SizeLatLng getSize() {
        return new SizeLatLng(this.getHeightLat(), this.getWidthLng());
    }

    public void setSize(SizeLatLng value) {
        this.setWidthLng(value.getWidthLng());
        this.setHeightLat(value.getHeightLat());
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double value) {
        this.lng = value;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double value) {
        this.lat = value;
    }

    public double getWidthLng() {
        return this.widthLng;
    }

    public void setWidthLng(double value) {
        this.widthLng = value;
    }

    public double getHeightLat() {
        return this.heightLat;
    }

    public void setHeightLat(double value) {
        this.heightLat = value;
    }

    public double getLeft() {
        return this.getLng();
    }

    public double getTop() {
        return this.getLat();
    }

    public double getRight() {
        return (this.getLng() + this.getWidthLng());
    }

    public double getBottom() {
        return (this.getLat() - this.getHeightLat());
    }

    private boolean NotEmpty;

    public boolean getIsEmpty() {
        return !NotEmpty;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RectLatLng)) {
            return false;
        }
        RectLatLng ef = (RectLatLng) obj;
        return ((((ef.getLng() == this.getLng()) && (ef.getLat() == this
                .getLat())) && (ef.getWidthLng() == this.getWidthLng())) && (ef
                .getHeightLat() == this.getHeightLat()));
    }

    public static boolean OpEquality(RectLatLng left, RectLatLng right) {
        return ((((left.getLng() == right.getLng()) && (left.getLat() == right
                .getLat())) && (left.getWidthLng() == right.getWidthLng())) && (left
                .getHeightLat() == right.getHeightLat()));
    }

    public static boolean OpInequality(RectLatLng left, RectLatLng right) {
        return !(RectLatLng.OpEquality(left, right));
    }

    public boolean Contains(double lat, double lng) {
        return ((((this.getLng() <= lng) && (lng < (this.getLng() + this
                .getWidthLng()))) && (this.getLat() >= lat)) && (lat > (this
                .getLat() - this.getHeightLat())));
    }

    public boolean Contains(PointLatLng pt) {
        return this.Contains(pt.getLat(), pt.getLng());
    }

    public boolean Contains(RectLatLng rect) {
        return ((((this.getLng() <= rect.getLng()) && ((rect.getLng() + rect
                .getWidthLng()) <= (this.getLng() + this.getWidthLng()))) && (this
                .getLat() >= rect.getLat())) && ((rect.getLat() - rect
                .getHeightLat()) >= (this.getLat() - this.getHeightLat())));
    }

    @Override
    public int hashCode() {
        if (this.getIsEmpty()) {
            return 0;
        }
        return ((((new Double(this.getLng())).hashCode() ^ (new Double(
                this.getLat())).hashCode()) ^ (new Double(this.getWidthLng()))
                .hashCode()) ^ (new Double(this.getHeightLat())).hashCode());
    }

    public void Inflate(double lat, double lng) {
        this.setLng(this.getLng() - lng);
        this.setLat(this.getLat() + lat);
        this.setWidthLng(this.getWidthLng() + 2d * lng);
        this.setHeightLat(this.getHeightLat() + 2d * lat);
    }

    public void Inflate(SizeLatLng size) {
        this.Inflate(size.getHeightLat(), size.getWidthLng());
    }

    public static RectLatLng Inflate(RectLatLng rect, double lat, double lng) {
        RectLatLng ef = rect;
        ef.Inflate(lat, lng);
        return ef;
    }

    public void Intersect(RectLatLng rect) {
        RectLatLng ef = Intersect(rect, this);
        this.setLng(ef.getLng());
        this.setLat(ef.getLat());
        this.setWidthLng(ef.getWidthLng());
        this.setHeightLat(ef.getHeightLat());
    }

    public static RectLatLng Intersect(RectLatLng a, RectLatLng b) {
        double lng = Math.max(a.getLng(), b.getLng());
        double num2 = Math.min((double) (a.getLng() + a.getWidthLng()),
                (double) (b.getLng() + b.getWidthLng()));

        double lat = Math.max(a.getLat(), b.getLat());
        double num4 = Math.min((double) (a.getLat() + a.getHeightLat()),
                (double) (b.getLat() + b.getHeightLat()));

        if ((num2 >= lng) && (num4 >= lat)) {
            return new RectLatLng(lat, lng, num2 - lng, num4 - lat);
        }
        return Empty;
    }

    public boolean IntersectsWith(RectLatLng a) {
        return this.getLeft() < a.getRight() && this.getTop() > a.getBottom()
                && this.getRight() > a.getLeft()
                && this.getBottom() < a.getTop();
    }

    public static RectLatLng Union(RectLatLng a, RectLatLng b) {
        return RectLatLng.FromLTRB(Math.min(a.getLeft(), b.getLeft()),
                Math.max(a.getTop(), b.getTop()),
                Math.max(a.getRight(), b.getRight()),
                Math.min(a.getBottom(), b.getBottom()));
    }

    public void Offset(PointLatLng pos) {
        this.Offset(pos.getLat(), pos.getLng());
    }

    public void Offset(double lat, double lng) {
        this.setLng(this.getLng() + lng);
        this.setLat(this.getLat() - lat);
    }

    @Override
    public String toString() {
        return ("{Lat=" + this.getLat() + ",Lng=" + this.getLng()
                + ",WidthLng=" + this.getWidthLng() + ",HeightLat="
                + this.getHeightLat() + "}");
    }

    public RectLatLng clone() {
        RectLatLng varCopy = new RectLatLng();

        varCopy.lng = this.lng;
        varCopy.lat = this.lat;
        varCopy.widthLng = this.widthLng;
        varCopy.heightLat = this.heightLat;
        varCopy.NotEmpty = this.NotEmpty;

        return varCopy;
    }
}