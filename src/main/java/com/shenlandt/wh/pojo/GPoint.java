package com.shenlandt.wh.pojo;

/**
 * GPoint 点抽像
 * <p>
 * Version: 1.0
 */
public class GPoint {

    public static final GPoint Empty = new GPoint();

    private long x;
    private long y;

    public GPoint() {
    }

    public GPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public GPoint(GSize sz) {
        this.x = sz.getWidth();
        this.y = sz.getHeight();
    }

    public boolean getIsEmpty() {
        return x == 0 && y == 0;
    }

    public long getX() {
        return x;
    }

    public void setX(long value) {
        x = value;
    }

    public long getY() {
        return y;
    }

    public void setY(long value) {
        y = value;
    }

    public static GPoint OpAddition(GPoint pt, GSize sz) {
        return Add(pt, sz);
    }

    public static GPoint OpSubtraction(GPoint pt, GSize sz) {
        return Subtract(pt, sz);
    }

    public static boolean OpEquality(GPoint left, GPoint right) {
        return left.getX() == right.getX() && left.getY() == right.getY();
    }

    public static boolean OpInequality(GPoint left, GPoint right) {
        return !(GPoint.OpEquality(left, right));
    }

    public static GPoint Add(GPoint pt, GSize sz) {
        return new GPoint(pt.getX() + sz.getWidth(), pt.getY() + sz.getHeight());
    }

    public static GPoint Subtract(GPoint pt, GSize sz) {
        return new GPoint(pt.getX() - sz.getWidth(), pt.getY() - sz.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GPoint)) {
            return false;
        }
        GPoint comp = (GPoint) obj;
        return comp.getX() == this.getX() && comp.getY() == this.getY();
    }

    @Override
    public int hashCode() {
        return (int) (x ^ y);
    }

    public void Offset(long dx, long dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    public void Offset(GPoint p) {
        Offset(p.getX(), p.getY());
    }

    public void OffsetNegative(GPoint p) {
        Offset(-p.getX(), -p.getY());
    }

    @Override
    public String toString() {
        return "{X=" + getX() + ",Y=" + getY() + "}";
    }

    public GPoint clone() {
        GPoint varCopy = new GPoint();

        varCopy.x = this.x;
        varCopy.y = this.y;

        return varCopy;
    }
}