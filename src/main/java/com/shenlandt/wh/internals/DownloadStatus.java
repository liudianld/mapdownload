package com.shenlandt.wh.internals;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shenlandt.wh.providers.GMapProvider;

/**
 * 瓦片下载状态
 * 
 * <p/>
 * <p>Version: 1.0
 */
public class DownloadStatus {
    private int tileCount;
    private int downCount;
    private int failCount;
    private GMapProvider provider;
    private List<String> urls = Lists.newArrayList();
    private List<String> failUrls = Lists.newArrayList();
    private List<LevelStatus> lss = Lists.newArrayList();
    
    public Map<String, Integer> toJSON() {
        Map<String, Integer> map = Maps.newHashMap();
        map.put("tileCount", tileCount);
        map.put("downCount", downCount);
        map.put("failCount", failCount);
        return map;
    }
    
    public DownloadStatus(GMapProvider provider) {
        this.provider = provider;
    }
    
    public DownloadStatus(int tileCount, int downCount, int failCount) {
        this.tileCount = tileCount;
        this.downCount = downCount;
        this.failCount = failCount;
    }

    /**
     * 已下载的瓦片数加一
     */
    public void addDownCount(){
        this.downCount++;
    }
    
    /**
     * 下载失败数加一
     */
    public void addFailCount(){
        this.failCount++;
    }
    
    /**
     * 检查所有瓦片是否下载完成
     * @return
     */
    public boolean isDone(){
        if(this.tileCount == this.downCount+this.failCount)
            return true;
        return false;
    }
    
    /**
     * 已下载的瓦片总数(不含失败数)
     * @return
     */
    public int downCount(){
        if(isDone())
            return this.downCount;
        return -1;
    }
    
    /**
     * 下载失败瓦片数
     * @return
     */
    public int failCount(){
        if(isDone())
            return this.failCount;
        return -1;
    }
    
    /**
     * 瓦片总数
     * @return
     */
    public int getCount(){
        return this.tileCount;
    }
    
    /**
     * 下载失败瓦片的URL
     * @return
     */
    public List<String> getFailUrl(){
        if(isDone())
            return this.failUrls;
        return null;
    }

    /**
     * 下载所用的地图provider
     * @return
     */
    public GMapProvider getProvider() {
        return provider;
    }

    /**
     * 所有下载链接
     * @return
     */
    public List<String> getUrls() {
        return urls;
    }

    /**
     * 取得所有待下载的级别
     * @return
     */
    public List<LevelStatus> getLss() {
        return lss;
    }

    /**
     * 增加一个待下载级别
     * @param lss
     */
    public void addLevelStatus(LevelStatus ls) {
        this.lss.add(ls);
        
        this.tileCount += ls.getCount();
    }


    public int getTileCount() {
        return tileCount;
    }
}
