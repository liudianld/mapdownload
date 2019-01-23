package com.shenlandt.wh.pojo;

/**
 * GSize 图片大小抽像
 * 
 * <p/>
 * <p>User: weiq
 * <p>Date: 2015年11月5日 下午5:07:32 
 * <p>Version: 1.0
 */
public class GSize {
    public static final GSize Empty = new GSize();

    private long width;
    private long height;

    public GSize() {
    }

    public GSize(GPoint pt) {
        width = pt.getX();
        height = pt.getY();
    }

    public GSize(long width, long height) {
        this.width = width;
        this.height = height;
    }

    public static GSize OpAddition(GSize sz1, GSize sz2) {
        return Add(sz1, sz2);
    }

    public static GSize OpSubtraction(GSize sz1, GSize sz2) {
        return Subtract(sz1, sz2);
    }

    public static boolean OpEquality(GSize sz1, GSize sz2) {
        return sz1.getWidth() == sz2.getWidth()
                && sz1.getHeight() == sz2.getHeight();
    }

    public static boolean OpInequality(GSize sz1, GSize sz2) {
        return !(GSize.OpEquality(sz1, sz2));
    }

    public boolean getIsEmpty() {
        return width == 0 && height == 0;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long value) {
        width = value;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long value) {
        height = value;
    }

    public static GSize Add(GSize sz1, GSize sz2) {
        return new GSize(sz1.getWidth() + sz2.getWidth(), sz1.getHeight()
                + sz2.getHeight());
    }

    public static GSize Subtract(GSize sz1, GSize sz2) {
        return new GSize(sz1.getWidth() - sz2.getWidth(), sz1.getHeight()
                - sz2.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GSize)) {
            return false;
        }

        GSize comp = (GSize) obj;
        return (comp.width == this.width) && (comp.height == this.height);
    }

    @Override
    public int hashCode() {
        if (this.getIsEmpty()) {
            return 0;
        }
        return ((new Long(getWidth())).hashCode() ^ (new Long(getHeight()))
                .hashCode());
    }

    @Override
    public String toString() {
        return "{Width=" + width + ", Height=" + height + "}";
    }

    public GSize clone() {
        GSize varCopy = new GSize();

        varCopy.width = this.width;
        varCopy.height = this.height;

        return varCopy;
    }
}