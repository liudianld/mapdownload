package com.shenlandt.wh.pojo;

/**
 * 瓦片类型 普通地图, 卫星地图, 地型地图, 混合地图
 * 
 */
public enum TileTypeEnum {
    ROAD("road","普通地图"),SATELLITE("sate","卫星地图"),TERRAIN("terrain","地型地图"),HYBRID("hybrid","混合地图");
    
    private String name;
    private String info;
    
    private TileTypeEnum(String name, String info){
        this.name = name;
        this.info = info;
    }
    
    public String getName(){
        return name;
    }
    
    public String getInfo(){
        return info;
    }
    
}
