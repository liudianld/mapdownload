package com.shenlandt.wh.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shenlandt.wh.pojo.DownloadInfo;
import com.shenlandt.wh.pojo.SearchResult;
import com.shenlandt.wh.service.MapDownloadService;


@RestController
public class MapDownloadController {
	
    @Autowired
    MapDownloadService mapDownloadService;
    
	@PostMapping("/downloadTile")
	@ResponseBody
    public HashMap<Object, Object> downloadTile(@RequestBody DownloadInfo di) throws IOException{
		HashMap<Object, Object> ret = new HashMap<>();
        if("all".equals(di.getDownType()))
        	ret = mapDownloadService.downloadTile(di);
        else if("fail".equals(di.getDownType()))
        	ret = mapDownloadService.downloadFailTile(di);
		return ret;
    }
	
	@GetMapping("/search")
	@ResponseBody
    public SearchResult searchPoi(@RequestParam String search, @RequestParam int limit, @RequestParam int offset) throws Exception{
		return mapDownloadService.searchPoi(search, limit, offset);
    }
}
