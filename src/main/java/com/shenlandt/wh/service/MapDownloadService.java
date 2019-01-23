package com.shenlandt.wh.service;

import java.io.IOException;
import java.util.HashMap;

import com.shenlandt.wh.pojo.DownloadInfo;

public interface MapDownloadService {

	HashMap<Object, Object> downloadTile(DownloadInfo di) throws IOException;

	HashMap<Object, Object> downloadFailTile(DownloadInfo di) throws IOException;

}
