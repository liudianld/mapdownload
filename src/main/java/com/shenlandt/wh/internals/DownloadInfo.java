package com.shenlandt.wh.internals;

import java.util.List;

import com.shenlandt.wh.pojo.PointLatLng;

/**
 * 地图下载信息对象
 * 
 * <p/>
 * <p>Version: 1.0
 */
public class DownloadInfo {
    private int fromZoom;
    private int toZoom;
    private List<PointLatLng> latLngs;
    private String providerName;
    private String downType; //all:下载所有瓦片; fail:下载失败的瓦片
    private int failRepeat;  //失败重试次数
    
    public int getFromZoom() {
        return fromZoom;
    }
    public int getToZoom() {
        return toZoom;
    }
    public List<PointLatLng> getLatLngs() {
        return latLngs;
    }
    public void setFromZoom(int fromZoom) {
        this.fromZoom = fromZoom;
    }
    public void setToZoom(int toZoom) {
        this.toZoom = toZoom;
    }
    public void setLatLngs(List<PointLatLng> latLngs) {
        this.latLngs = latLngs;
    }
    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    public String getDownType() {
        return downType;
    }
    public void setDownType(String downType) {
        this.downType = downType;
    }
    public int getFailRepeat() {
        return failRepeat;
    }
    public void setFailRepeat(int failRepeat) {
        this.failRepeat = failRepeat;
    }
}
