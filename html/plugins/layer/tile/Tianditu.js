/**
 * 天地图
 */
L.TiandituLayer = L.TileLayer.extend({
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
            "http://webrd0{s}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7&x={x}&y={y}&z={z}"  ;
        var urlArgs = {
            z: tilePoint.z,
            x: tilePoint.x,
            y: tilePoint.y
        };
        switch(this._type){
        case 'ROADMAP':
            break;
        case 'SATELLITE':
            this._url = "http://webst0{s}.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}";
            break;
        case 'HYBRID':
            this._url = "http://webst0{s}.is.autonavi.com/appmaptile?style=8&x={x}&y={y}&z={z}";
            break;
        }
        
        return L.Util.template(this._url, L.extend(urlArgs, this.options, {s: this._getSubdomain(tilePoint)}));
    }
});

L.tiandituLayer = function (key, options) {
    return new L.TiandituLayer(key, options);
};
