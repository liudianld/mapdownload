package com.shenlandt.wh.pojo;

import java.io.Serializable;

public class PointLatLng implements Serializable {
	private static final long serialVersionUID = 1465743939571047660L;
	public static final PointLatLng Empty = new PointLatLng();
	private double lat;
	private double lng;

	private boolean notEmpty;

	public PointLatLng() {
	}

	public PointLatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
		this.notEmpty = true;
	}

	/**
	 * 坐标未分配返回true
	 */
	public boolean getIsEmpty() {
		return !notEmpty;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(double value) {
		this.lat = value;
		this.notEmpty = true;
	}

	public double getLng() {
		return this.lng;
	}

	public void setLng(double value) {
		this.lng = value;
		this.notEmpty = true;
	}

	public static PointLatLng OpAddition(PointLatLng pt, SizeLatLng sz) {
		return Add(pt, sz);
	}

	public static PointLatLng OpSubtraction(PointLatLng pt, SizeLatLng sz) {
		return Subtract(pt, sz);
	}

	public static SizeLatLng OpSubtraction(PointLatLng pt1, PointLatLng pt2) {
		return new SizeLatLng(pt1.getLat() - pt2.getLat(), pt2.getLng() - pt1.getLng());
	}

	public static boolean OpEquality(PointLatLng left, PointLatLng right) {
		return ((left.getLng() == right.getLng()) && (left.getLat() == right.getLat()));
	}

	public static boolean OpInequality(PointLatLng left, PointLatLng right) {
		return !(PointLatLng.OpEquality(left, right));
	}

	public static PointLatLng Add(PointLatLng pt, SizeLatLng sz) {
		return new PointLatLng(pt.getLat() - sz.getHeightLat(), pt.getLng() + sz.getWidthLng());
	}

	public static PointLatLng Subtract(PointLatLng pt, SizeLatLng sz) {
		return new PointLatLng(pt.getLat() + sz.getHeightLat(), pt.getLng() - sz.getWidthLng());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PointLatLng)) {
			return false;
		}
		PointLatLng tf = (PointLatLng) obj;
		return (((tf.getLng() == this.getLng()) && (tf.getLat() == this.getLat()))
				&& tf.getClass().equals(super.getClass()));
	}

	public void Offset(PointLatLng pos) {
		this.Offset(pos.getLat(), pos.getLng());
	}

	public void Offset(double lat, double lng) {
		this.setLng(this.getLng() + lng);
		this.setLat(this.getLat() - lat);
	}

	@Override
	public int hashCode() {
		return ((new Double(this.getLng())).hashCode() ^ (new Double(this.getLat())).hashCode());
	}

	@Override
	public String toString() {
		return String.format("{{Lat={0}, Lng={1}}}", this.getLat(), this.getLng());
	}

	public PointLatLng clone() {
		PointLatLng varCopy = new PointLatLng();

		varCopy.lat = this.lat;
		varCopy.lng = this.lng;
		varCopy.notEmpty = this.notEmpty;

		return varCopy;
	}
}