package com.shenlandt.wh.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.shenlandt.wh.pojo.DownloadInfo;
import com.shenlandt.wh.pojo.SearchResult;

public interface MapDownloadService {

	HashMap<Object, Object> downloadTile(DownloadInfo di) throws IOException;

	HashMap<Object, Object> downloadFailTile(DownloadInfo di) throws IOException;

	/**
	 * 根据关键字查询位置信息列表
	 * @param searchString 关键字
	 * @param limit 
	 * @param offset 
	 * @return
	 * @throws Exception
	 */
	SearchResult searchPoi(String searchString, int limit, int offset) throws Exception;

}
