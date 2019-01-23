package com.shenlandt.wh.providers.baidu;

import java.text.MessageFormat;
import java.util.Date;

import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.projections.MercatorProjection;
import com.shenlandt.wh.providers.GMapProvider;
import com.shenlandt.wh.utils.DateTimeUtil;

/**
 * 百度地图基类, 这个地图与其它地图叠加时不会完全重合.
 * 因为2B百度在GCJ02(火星坐标系)上又采用了 BD-09 坐标系(百度地图专用的坐标系)。 
 * <p/>
 * <p>User: weiq
 * <p>Date: 2015年10月19日 上午9:34:16 
 * <p>Version: 1.0
 */
public abstract class BaiduMapProviderBase extends GMapProvider{
    //原来：http://q3.baidu.com/it/u=x=721;y=209;z=12;v=014;type=web&fm=44
    //更新：http://online1.map.bdimg.com/tile/?qt=tile&x=23144&y=6686&z=17&styles=pl
    public static final String UrlFormat = "http://online{0}.map.bdimg.com/tile/?qt=tile&x={1}&y={2}&z={3}&styles=pl&udt=20150213";
    
    public static final Integer serverNum = 9;
    
    private GMapProvider[] overlays;
    
    public BaiduMapProviderBase(){
        this.MaxZoom = 19;
        this.MinZoom = 1;
        RefererUrl = MessageFormat.format("http://q{0}.baidu.com/", serverNum);
        Copyright = MessageFormat.format("©{0} Baidu Corporation, ©{0} NAVTEQ, ©{0} Image courtesy of NASA",
                DateTimeUtil.getDateExtract(new Date(), "y"));
    }
    
    @Override
    public PureProjection getProjection() {
        return MercatorProjection.Instance;
    }

    @Override
    public GMapProvider[] getOverlays() {
        if (overlays == null) {
            overlays = new GMapProvider[] { this };
        }
        return overlays;
    }
}
