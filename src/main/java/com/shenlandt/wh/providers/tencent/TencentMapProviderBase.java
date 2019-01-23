package com.shenlandt.wh.providers.tencent;

import java.text.MessageFormat;
import java.util.Date;

import com.shenlandt.wh.pojo.PureProjection;
import com.shenlandt.wh.projections.MercatorProjection;
import com.shenlandt.wh.providers.GMapProvider;
import com.shenlandt.wh.utils.DateTimeUtil;

/**
 * QQ地图基类
 * <p>Version: 1.0
 */
public abstract class TencentMapProviderBase extends GMapProvider{

    public static final Integer serverNum = 2; 
    
    private GMapProvider[] overlays;
    
    public TencentMapProviderBase(){
        this.MaxZoom = 19;
        this.MinZoom = 1;
        RefererUrl = MessageFormat.format("http://q{0}.baidu.com/", serverNum);
        Copyright = MessageFormat.format("©{0} 高德 Corporation, ©{0} NAVTEQ, ©{0} Image courtesy of NASA",
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
