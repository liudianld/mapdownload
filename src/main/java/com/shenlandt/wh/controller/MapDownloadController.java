package com.shenlandt.wh.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.shenlandt.wh.internals.DownloadStatus;
import com.shenlandt.wh.internals.LevelStatus;
import com.shenlandt.wh.internals.PureImage;
import com.shenlandt.wh.internals.TileStatus;
import com.shenlandt.wh.pojo.DownloadInfo;
import com.shenlandt.wh.pojo.GMaps;
import com.shenlandt.wh.pojo.GPoint;
import com.shenlandt.wh.pojo.PointLatLng;
import com.shenlandt.wh.pojo.RectLatLng;
import com.shenlandt.wh.providers.GMapProvider;
import com.shenlandt.wh.providers.GMapProviders;
import com.shenlandt.wh.utils.FileUtil;
import com.shenlandt.wh.utils.MapUtil;


@RestController
public class MapDownloadController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(MapDownloadController.class);

	
	@GetMapping("/hello")
	public String hello() {
		return "hello world!";
	}
	
	@PostMapping("/downloadTile")
	@ResponseBody
    public void downloadTile(@RequestBody DownloadInfo di){
    	System.out.println("downloadTile.............................");
        List<PointLatLng> pointLatLngs = di.getLatLngs();
        
        if(pointLatLngs==null){
            LOGGER.error("未取得多边形的范围,点集为空或者不能组成多边形");
            return;
        }

        int minZoom = di.getFromZoom();
        int maxZoom = di.getToZoom();
        int currentZoom = minZoom;
        String providerName = di.getProviderName(); //"GoogleChinaMap";
        
        RectLatLng rect = MapUtil.getBounds(pointLatLngs);
        
        GMapProvider provider = GMapProviders.Instance.tryGetProvider(providerName);
        if(provider==null){
            LOGGER.error("provider name : "+providerName+" is null !");
            HashMap<Object, Object> sendMap = Maps.newHashMap();
            sendMap.put("isError", true);
            sendMap.put("content", "provider name : "+providerName+" is null !");
            
//            WebSockets.sendText(JSONHelper.toJSONString(sendMap), channel, null);
        }else{
            //ThreadWaiter.reset();
            //收集下载信息
            int jumpCount = 0;
            int totalCount = 0;
            DownloadStatus ds = new DownloadStatus(provider);
            while(currentZoom <= maxZoom){
                List<GPoint> points = provider.getProjection().GetAreaTileList(rect, currentZoom, 0);
                int len = points.size();
                totalCount += len;
                LevelStatus ls = new LevelStatus(currentZoom, ds);
                for(int i=0; i<len; i++){
                    //加入前检查一下这个图片是否存在
                    String cachePath = provider.getCachePath(currentZoom, points.get(i));
                    if(FileUtil.isExist(cachePath)){
                        LOGGER.info("跳过,因为瓦片存在,"+cachePath);
                        jumpCount++;
                        continue;
                    }
                    String url = provider.GetTileImageUrl(points.get(i), currentZoom);
                    TileStatus ts = new TileStatus(ls,points.get(i),url);
                    ls.addTileStatus(ts);
                }
                ds.addLevelStatus(ls);
                currentZoom++;
            }
            
            //如果这个区域的瓦片全部跳过,即已经存在.
            if(jumpCount == totalCount){
                HashMap<Object, Object> sendMap = Maps.newHashMap();
                sendMap.put("isError", true);
                sendMap.put("title", "已下载");
                sendMap.put("content", "此范围无需再次下载!");
//                WebSockets.sendText(JSONHelper.toJSONString(sendMap), channel, null);
                return;
            }
            
            //开始循环下载
            if(ds.getTileCount()==0){
                HashMap<Object, Object> sendMap = Maps.newHashMap();
                sendMap.put("isError", true);
                sendMap.put("content", "待下载数量为0 期望值>0 !");
//                WebSockets.sendText(JSONHelper.toJSONString(sendMap), channel, null);
                return;
            }
            
            List<LevelStatus> lss = ds.getLss();
            for(LevelStatus ls : lss){
                List<TileStatus> tss = ls.getTss();
                for(TileStatus ts : tss){
//                    GMaps.http.get(ts.getUrl(), callbackTrace.trace(binCallback), ts, channel, ts.getPoint());
                	PureImage img = GMaps.getInstance().GetImageFrom(provider, ts.getPoint(), ts.getLs().getLevel());
                	GMaps.getInstance().saveLR(ts.getLs().getLevel(), ts.getPoint(), img);
                }
            }
            //ThreadWaiter.waitingThreads();
        }
    }
}
