package com.shenlandt.wh.internals;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 分级瓦片状态
 */
public class LevelStatus{
    private int level=-1;
    private int downCount;
    private int failCount;
    private DownloadStatus ds;
    private List<TileStatus> tss = Lists.newArrayList();

    public LevelStatus(int level, DownloadStatus ds) {
        this.level = level;
        this.downCount = 0;
        this.failCount = 0;
        this.ds = ds;
    }

    /**
     * 级别
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     * 瓦片总数
     * @return
     */
    public int getCount() {
        return this.tss.size();
    }
    
    /**
     * 改变待处理的瓦片总数
     */
    public void changeCount(){
        
    }

    /**
     * 已下载的瓦片数
     */
    public int getDownCount() {
        return downCount;
    }
    
    /**
     * 已下载的瓦片数加一
     */
    public void addDownCount(){
        this.ds.addDownCount();
        this.downCount++;
    }
    
    /**
     * 下载失败数加一
     */
    public void addFailCount(){
        this.ds.addFailCount();
        this.failCount++;
    }
    
    /**
     * 这个级别下的瓦片是否下载完成
     * @return
     */
    public boolean isDone(){
        if(this.getCount() == this.downCount+this.failCount)
            return true;
        
        return false;
    }

    /**
     * 取得List TileStatus
     * @return
     */
    public List<TileStatus> getTss() {
        return tss;
    }
    
    public void addTileStatus(TileStatus ts){
        this.tss.add(ts);
    }
    
    /**
     * 取得DownloadStatus
     * @return
     */
    public DownloadStatus getDs() {
        return ds;
    }
}
