package com.shenlandt.wh.providers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shenlandt.wh.providers.amap.AMapHybridMapProvider;
import com.shenlandt.wh.providers.amap.AMapProvider;
import com.shenlandt.wh.providers.amap.AMapSatelliteMapProvider;
import com.shenlandt.wh.providers.baidu.BaiduHybridMapProvider;
import com.shenlandt.wh.providers.baidu.BaiduMapProvider;
import com.shenlandt.wh.providers.baidu.BaiduSatelliteMapProvider;
import com.shenlandt.wh.providers.google.GoogleHybridMapProvider;
import com.shenlandt.wh.providers.google.GoogleMapProvider;
import com.shenlandt.wh.providers.google.GoogleSatelliteMapProvider;
import com.shenlandt.wh.providers.google.GoogleTerrainMapProvider;
import com.shenlandt.wh.providers.google.China.GoogleChinaHybridMapProvider;
import com.shenlandt.wh.providers.google.China.GoogleChinaMapProvider;
import com.shenlandt.wh.providers.google.China.GoogleChinaSatelliteMapProvider;
import com.shenlandt.wh.providers.google.China.GoogleChinaTerrainMapProvider;
import com.shenlandt.wh.providers.tencent.TencentHybridMapProvider;
import com.shenlandt.wh.providers.tencent.TencentMapProvider;
import com.shenlandt.wh.providers.tencent.TencentSatelliteMapProvider;
import com.shenlandt.wh.utils.Reflections;

/** 
 * GMapProviders
 */
public class GMapProviders
{
    public static final GMapProviders Instance = new GMapProviders();
    
    public static GMapProviders getInstance() {
        return Instance;
    }
    
	private GMapProviders()
	{
	    list = new ArrayList<GMapProvider>();
	    map = new HashMap<>();
        try {
            Class type = GMapProviders.class;
            for (Field p : type.getFields())
            {
                Object tempVar = Reflections.getFieldValue(this, p.getName());
                GMapProvider v = (GMapProvider)((tempVar instanceof GMapProvider) ? tempVar : null);
                if (v != null)
                {
                    list.add(v);
                    
                    map.put(v.getName(), v);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } 
	}

	public GoogleMapProvider GoogleMap = GoogleMapProvider.Instance;
	public GoogleSatelliteMapProvider GoogleSatelliteMap = GoogleSatelliteMapProvider.Instance;
	public GoogleHybridMapProvider GoogleHybridMap = GoogleHybridMapProvider.Instance;
	public GoogleTerrainMapProvider GoogleTerrainMap = GoogleTerrainMapProvider.Instance;

	public GoogleChinaMapProvider GoogleChinaMap = GoogleChinaMapProvider.Instance;
	public GoogleChinaSatelliteMapProvider GoogleChinaSatelliteMap = GoogleChinaSatelliteMapProvider.Instance;
	public GoogleChinaHybridMapProvider GoogleChinaHybridMap = GoogleChinaHybridMapProvider.Instance;
	public GoogleChinaTerrainMapProvider GoogleChinaTerrainMap = GoogleChinaTerrainMapProvider.Instance;
	
	public BaiduMapProvider BaiduMap = BaiduMapProvider.Instance;
	public BaiduSatelliteMapProvider BaiduSatelliteMap = BaiduSatelliteMapProvider.Instance;
    public BaiduHybridMapProvider BaiduHybridMap = BaiduHybridMapProvider.Instance;
    
    public AMapProvider AMap = AMapProvider.Instance;
    public AMapHybridMapProvider AMapHybridMap = AMapHybridMapProvider.Instance;
    public AMapSatelliteMapProvider AMapSatelliteMap = AMapSatelliteMapProvider.Instance;
    
    public TencentMapProvider TencentMap = TencentMapProvider.Instance;
    public TencentSatelliteMapProvider TencentSatelliteMap = TencentSatelliteMapProvider.Instance;
    public TencentHybridMapProvider TencentHybridMap = TencentHybridMapProvider.Instance;

	private static List<GMapProvider> list;
	private static Map<String,GMapProvider> map;

	/** 
	 * 得到所有provider实例
	 */
	public List<GMapProvider> getList() {
		return list;
	}
	
	/**
	 * 尝试使用provider的名字获得provider
	 * @param providerName
	 * @return
	 */
	public GMapProvider tryGetProvider(String providerName){
	    if(!map.isEmpty())
	        return map.get(providerName);
	    else
	        return null;
	}
}