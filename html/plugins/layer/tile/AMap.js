/**
 * 高德地图
 */
L.AMapLayer = L.TileLayer.extend({
    options: {
        subdomains: [1, 2, 3, 4]
    },
    
    //type: ROADMAP(普通地图), SATELLITE(卫星), HYBRID(混合)
    initialize: function(type, options) {
        L.Util.setOptions(this, options);
        
        this._type = type || 'ROADMAP';
    },
    
    getTileUrl: function (tilePoint) {
        this._url = 
            "http://wprd0{s}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=7";
        var urlArgs = {
            z: tilePoint.z,
            x: tilePoint.x,
            y: tilePoint.y
        };
        switch(this._type){
        case 'ROADMAP':
            break;
        case 'SATELLITE':
            this._url = "http://wprd0{s}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=6";
            break;
        case 'HYBRID':
            this._url = "http://wprd0{s}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scl=1&style=8";
            break;
        }
        
        return L.Util.template(this._url, L.extend(urlArgs, this.options, {s: this._getSubdomain(tilePoint)}));
    }
});

L.amapLayer = function (key, options) {
    return new L.AMapLayer(key, options);
};
