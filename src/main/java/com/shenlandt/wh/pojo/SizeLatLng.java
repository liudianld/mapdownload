package com.shenlandt.wh.pojo;

/**
 * SizeLatLng
 * 
 * Version: 1.0
 */
public final class SizeLatLng {
	public static SizeLatLng Empty = new SizeLatLng();;

	private double heightLat;
	private double widthLng;

	public SizeLatLng() {

	}

	public SizeLatLng(SizeLatLng size) {
		this.widthLng = size.widthLng;
		this.heightLat = size.heightLat;
	}

	public SizeLatLng(PointLatLng pt) {
		this.heightLat = pt.getLat();
		this.widthLng = pt.getLng();
	}

	public SizeLatLng(double heightLat, double widthLng) {
		this.heightLat = heightLat;
		this.widthLng = widthLng;
	}

	public static SizeLatLng OpAddition(SizeLatLng sz1, SizeLatLng sz2) {
		return Add(sz1, sz2);
	}

	public static SizeLatLng OpSubtraction(SizeLatLng sz1, SizeLatLng sz2) {
		return Subtract(sz1, sz2);
	}

	public static boolean OpEquality(SizeLatLng sz1, SizeLatLng sz2) {
		return ((sz1.getWidthLng() == sz2.getWidthLng()) && (sz1.getHeightLat() == sz2.getHeightLat()));
	}

	public static boolean OpInequality(SizeLatLng sz1, SizeLatLng sz2) {
		return !(SizeLatLng.OpEquality(sz1, sz2));
	}

	public boolean getIsEmpty() {
		return ((this.widthLng == 0d) && (this.heightLat == 0d));
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

	public static SizeLatLng Add(SizeLatLng sz1, SizeLatLng sz2) {
		return new SizeLatLng(sz1.getHeightLat() + sz2.getHeightLat(), sz1.getWidthLng() + sz2.getWidthLng());
	}

	public static SizeLatLng Subtract(SizeLatLng sz1, SizeLatLng sz2) {
		return new SizeLatLng(sz1.getHeightLat() - sz2.getHeightLat(), sz1.getWidthLng() - sz2.getWidthLng());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SizeLatLng)) {
			return false;
		}
		SizeLatLng ef = (SizeLatLng) obj;
		return (((ef.getWidthLng() == this.getWidthLng()) && (ef.getHeightLat() == this.getHeightLat()))
				&& ef.getClass().equals(super.getClass()));
	}

	@Override
	public int hashCode() {
		if (this.getIsEmpty()) {
			return 0;
		}
		return ((new Double(this.getWidthLng())).hashCode() ^ (new Double(this.getHeightLat())).hashCode());
	}

	@Override
	public String toString() {
		return ("{WidthLng=" + widthLng + ", HeightLng=" + heightLat + "}");
	}

	public SizeLatLng clone() {
		SizeLatLng varCopy = new SizeLatLng();

		varCopy.heightLat = this.heightLat;
		varCopy.widthLng = this.widthLng;

		return varCopy;
	}
}