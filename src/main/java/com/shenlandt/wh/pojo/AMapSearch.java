package com.shenlandt.wh.pojo;

import java.util.List;

public class AMapSearch {
	private String status;
	private String count;
	private String info;
	private String infocode;
	private List<AmapSearchPois> pois;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfocode() {
		return infocode;
	}

	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}

	public List<AmapSearchPois> getPois() {
		return pois;
	}

	public void setPois(List<AmapSearchPois> pois) {
		this.pois = pois;
	}

}
