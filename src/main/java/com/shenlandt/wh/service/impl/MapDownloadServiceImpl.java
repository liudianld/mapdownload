package com.shenlandt.wh.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
import com.shenlandt.wh.service.MapDownloadService;
import com.shenlandt.wh.utils.FileUtil;
import com.shenlandt.wh.utils.FileUtils;
import com.shenlandt.wh.utils.MapUtil;

@Service
public class MapDownloadServiceImpl implements MapDownloadService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(MapDownloadServiceImpl.class);

	@Override
	public HashMap<Object, Object> downloadTile(DownloadInfo di) throws IOException {
		System.out.println("downloadTile.............................");
		HashMap<Object, Object> sendMap = Maps.newHashMap();
		DownloadStatus ds = null;

		List<PointLatLng> pointLatLngs = di.getLatLngs();

		if (pointLatLngs == null) {
			LOGGER.error("未取得多边形的范围,点集为空或者不能组成多边形");
			return null;
		}

		int minZoom = di.getFromZoom();
		int maxZoom = di.getToZoom();
		int currentZoom = minZoom;
		String providerName = di.getProviderName(); // "GoogleChinaMap";

		RectLatLng rect = MapUtil.getBounds(pointLatLngs);

		GMapProvider provider = GMapProviders.Instance.tryGetProvider(providerName);
		if (provider == null) {
			LOGGER.error("provider name : " + providerName + " is null !");
			sendMap.put("isError", true);
			sendMap.put("content", "provider name : " + providerName + " is null !");

		} else {
			// 收集下载信息
			int jumpCount = 0;
			int totalCount = 0;
			ds = new DownloadStatus(provider);
			while (currentZoom <= maxZoom) {
				List<GPoint> points = provider.getProjection().GetAreaTileList(rect, currentZoom, 0);
				int len = points.size();
				totalCount += len;
				LevelStatus ls = new LevelStatus(currentZoom, ds);
				for (int i = 0; i < len; i++) {
					// 加入前检查一下这个图片是否存在
					String cachePath = provider.getCachePath(currentZoom, points.get(i));
					if (FileUtil.isExist(cachePath)) {
						LOGGER.info("跳过,因为瓦片存在," + cachePath);
						jumpCount++;
						continue;
					}
					String url = provider.GetTileImageUrl(points.get(i), currentZoom);
					TileStatus ts = new TileStatus(ls, points.get(i), url);
					ls.addTileStatus(ts);
				}
				ds.addLevelStatus(ls);
				currentZoom++;
			}

			// 如果这个区域的瓦片全部跳过,即已经存在.
			if (jumpCount == totalCount) {
				sendMap.put("isError", true);
				sendMap.put("title", "已下载");
				sendMap.put("content", "此范围无需再次下载!");
				return sendMap;
			}

			// 开始循环下载
			if (ds.getTileCount() == 0) {
				sendMap.put("isError", true);
				sendMap.put("content", "待下载数量为0 期望值>0 !");
				return sendMap;
			}

			List<LevelStatus> lss = ds.getLss();
			for (LevelStatus ls : lss) {
				List<TileStatus> tss = ls.getTss();
				for (TileStatus ts : tss) {
					LOGGER.info("开始下载瓦片，下载地址 " + ts.getUrl());
					try{
						PureImage img = GMaps.getInstance().GetImageFrom(provider, ts.getPoint(), ts.getLs().getLevel());
						ts.getLs().getDs().getProvider().CacheImage(ts.getLs().getLevel(), ts.getPoint(), img);
						ts.isDown(true);
					}catch (IOException e) {
						LOGGER.error(e.getMessage());
						ts.isDown(false);
						this.onDownloadFail(ts.getUrl(), ts);
					}
				}
			}
			sendMap.put("isError", false);
			sendMap.put("isDone", true);
			sendMap.put("status", ds.toJSON());
		}
		return sendMap;
	}

	@Override
	public HashMap<Object, Object> downloadFailTile(DownloadInfo di) throws IOException {
		System.out.println("downloadFailTile..................");
        String providerName = di.getProviderName();
        GMapProvider provider = GMapProviders.Instance.tryGetProvider(providerName);
        String filePath = GMaps.Instance.tilePath+"/"+
                provider.getName() + "/downfail.m4j";
        List<String> fails = FileUtils.readAndDeleteFailList(filePath);
        int count = fails.size();
        int downCount = 0;
        int failCount = 0;
        for(String fail : fails){
            String[] temp = fail.split(";");
            String path = temp[0];
            String url = temp[1];
            try {
                provider.CacheImage(path, url);
                downCount++;
                LOGGER.info("失败列表中的 url:"+url+"下载成功,从文件中移出");
            } catch (IOException e) {
                FileUtils.logDownFail(filePath, path, url);
                failCount++;
                LOGGER.error("失败列表中的 url:"+url+"下载失败",e);
            }
        }
        
        DownloadStatus ds = new DownloadStatus(count, downCount, failCount);
        HashMap<Object, Object> sendMap = Maps.newHashMap();
        sendMap.put("isError", false);
        sendMap.put("isDone", true);
        sendMap.put("repeat", di.getFailRepeat()+1);
        sendMap.put("status", ds.toJSON());
//        WebSockets.sendText(JSONHelper.toJSONString(sendMap), channel, null);
        return sendMap;

	}
	
    /**
     * 处理下载失败
     * 失败后把下载失败的图片URL进行记录. 当全部瓦片下载完成后告知前台,
     * 由用户决定是否再次下载的图片.
     * @param url
     */
    public void onDownloadFail(String url, TileStatus ts) {
        ts.isDown(false);
//        isDone(ts, channel);
        
        //写入失败日志
        GMapProvider provider = ts.getLs().getDs().getProvider();
        
        String path = GMaps.Instance.tilePath+"/"+
                provider.getName() + "/downfail.m4j";
        
        String cachePath = provider.getCachePath(ts.getLs().getLevel(), ts.getPoint());
        FileUtils.logDownFail(path, cachePath, url);
        LOGGER.info(String.format("下载失败, URL: %s", url));
    }

}
