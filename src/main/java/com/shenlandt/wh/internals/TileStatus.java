package com.shenlandt.wh.internals;

import java.util.Map;

import com.google.common.collect.Maps;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.utils.JSONHelper;

/**
 * 瓦片状态
 * 
 * <p/>
 * <p>Version: 1.0
 */
public class TileStatus {
    private GPoint point;
    private String url;
    private Boolean isDown;
    private LevelStatus ls;
    
    public TileStatus(LevelStatus ls, GPoint point, String url) {
        this.ls = ls;
        this.point = point;
        this.url = url;
    }
    
    public GPoint getPoint() {
        return point;
    }
    public String getUrl() {
        return url;
    }
   
    public Boolean getIsDown() {
        return isDown;
    }
    public void setPoint(GPoint point) {
        this.point = point;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * 设置下载是否成功
     * @param isDown
     */
    public void isDown(Boolean isDown) {
        if(isDown)
            ls.addDownCount();
        else
            ls.addFailCount();
        
        this.isDown = isDown;
    }
    public LevelStatus getLs() {
        return ls;
    }
    
    @Override
    public String toString() {
        Map<String, Integer> map = Maps.newHashMap();
        map.put("currentZoom", this.ls.getLevel());
        map.put("tileNum", this.ls.getDownCount());
        map.put("tileCount", this.ls.getCount());
        
        return JSONHelper.toJSONString(map);
    }
}